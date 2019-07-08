package ru.otus.erinary.instrumentation;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("This is agent");
    }
}
