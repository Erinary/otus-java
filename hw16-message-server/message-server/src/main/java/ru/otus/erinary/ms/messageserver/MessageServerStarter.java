package ru.otus.erinary.ms.messageserver;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.erinary.ms.messageserver.service.MessageServer;

import java.util.Arrays;
import java.util.List;

public class MessageServerStarter {

    private static final Logger log = LoggerFactory.getLogger(MessageServerStarter.class);
    private static final String PORT_ARG = "port";
    private static final String CAPACITY_ARG = "capacity";
    private static final String QUEUES_ARG = "queues";

    public static void main(String[] args) {
        try {
            CommandLineParser cmdLineParser = new DefaultParser();
            CommandLine commandLine = cmdLineParser.parse(generateOptions(), args);

            List<String> queues = Arrays.asList(commandLine.getOptionValues(QUEUES_ARG));
            int port = ((Number) commandLine.getParsedOptionValue(PORT_ARG)).intValue();
            int capacity = commandLine.getOptionValue(CAPACITY_ARG) != null ?
                    Integer.parseInt(commandLine.getOptionValue(CAPACITY_ARG)) : 0;

            new MessageServer(queues, port,capacity).run();
        } catch (Exception e) {
            log.error("Error: ", e);
            System.exit(-1);
        }
    }

    private static Options generateOptions() {
        Option listenPort = Option.builder("p")
                .required()
                .hasArg()
                .longOpt(PORT_ARG)
                .type(Number.class)
                .build();
        Option capacity = Option.builder("c")
                .required(false)
                .hasArg()
                .longOpt(CAPACITY_ARG)
                .build();
        Option queuesOpt = Option.builder("q")
                .required()
                .hasArgs()
                .longOpt(QUEUES_ARG)
                .valueSeparator(',')
                .build();

        Options options = new Options();
        options.addOption(listenPort);
        options.addOption(capacity);
        options.addOption(queuesOpt);
        return options;
    }
}
