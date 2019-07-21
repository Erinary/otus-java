package ru.otus.erinary.instrumentation;

import org.objectweb.asm.*;
import ru.otus.erinary.annotation.Log;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class InstrumentationAgent {

    private static final int OPCODE = Opcodes.ASM5;

    public static void premain(String agentArgs, Instrumentation inst) {
        Set<String> loggedMethods = new HashSet<>();
        System.out.println("This is agent");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (!className.equals("ru/otus/erinary/annotation/Log")) {
                    MyProxyClassLoader classLoader = new MyProxyClassLoader();
                    for (Method method : classLoader.defineClass(className, classfileBuffer).getDeclaredMethods()) {
                        if (method.getAnnotation(Log.class) != null) {
                            loggedMethods.add(method.getName());
                        }
                    }
                    return addProxyMethod(classfileBuffer, loggedMethods, className);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass, Set<String> loggedMethods, String className) {
        ClassReader reader = new ClassReader(originalClass);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ClassVisitor(OPCODE, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (loggedMethods.contains(name)) {
                    return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };

        /*запуск синтаксического анализа байт-кода*/
        reader.accept(visitor, OPCODE);

        /*вызов операции конкатенации строк*/
        Handle handle = new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        for (String method : loggedMethods) {
            MethodVisitor methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC, method, "(Ljava/lang/String;)V", null, null);

            /*метод логирования*/
            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants",
                    "(Ljava/lang/String;)Ljava/lang/String;",
                    handle,
                    "[From premain] - params: [\u0001]"); //TODO строка "executed: %{methodName}, params: %{params}"
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            //TODO зависимость от кол-ва и типов аргументов
            /*вызов исходного метода + логирование*/
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, method + "Proxied", "(Ljava/lang/String;)V", false);

            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(0, 0);
        }
        byte[] finalClass = writer.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxy.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalClass;
    }
}
