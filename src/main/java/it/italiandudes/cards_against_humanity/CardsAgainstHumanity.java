package it.italiandudes.cards_against_humanity;

import it.italiandudes.cards_against_humanity.client.javafx.Client;
import it.italiandudes.cards_against_humanity.server.Server;
import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

public final class CardsAgainstHumanity {

    // Main Method
    public static void main(String[] args) {

        // Initializing the logger
        try {
            Logger.init();
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...");
            return;
        }

        // Configure the shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));


        if (Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.ProgramArguments.SERVER))) {
            Server.start();
        } else if (Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.ProgramArguments.DB_EDITOR))) {
            // TODO: Starts GUI DB Editor
        } else {
            try {
                Client.start(args);
            } catch (NoClassDefFoundError e) {
                Logger.log("ERROR: TO RUN THIS JAR YOU NEED JAVA 8 WITH BUILT-IN JAVAFX!", new InfoFlags(true, true, true, true));
                Logger.log(e);
                System.exit(0);
            }
        }
    }
}
