package it.italiandudes.cards_against_humanity.utils;

import it.italiandudes.cards_against_humanity.CardsAgainstHumanity;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public final class Defs {

    // App File Name
    public static final String APP_FILE_NAME = "CardsAgainstHumanity";

    // Charset
    public static final String DEFAULT_CHARSET = "UTF-8";

    // DB Versions
    public static final String DB_VERSION = "1.0";

    // Jar App Position
    public static final String JAR_POSITION;
    static {
        try {
            JAR_POSITION = new File(CardsAgainstHumanity.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // JSON Settings
    public static final class SettingsKeys {
        public static final String ENABLE_DARK_MODE = "enableDarkMode";
        public static final String ENABLE_DISCORD_RICH_PRESENCE = "enableDiscordRichPresence";
    }

    // Resources Location
    public static final class Resources {

        //Resource Getters
        public static URL get(@NotNull final String resourceConst) {
            return Objects.requireNonNull(CardsAgainstHumanity.class.getResource(resourceConst));
        }
        public static InputStream getAsStream(@NotNull final String resourceConst) {
            return Objects.requireNonNull(CardsAgainstHumanity.class.getResourceAsStream(resourceConst));
        }

        // JSON
        public static final class JSON {
            public static final String JSON_CLIENT_SETTINGS = "client_settings.json";
            public static final String JSON_SERVER_SETTINGS = "server_settings.json";
            public static final String DEFAULT_JSON_CLIENT_SETTINGS = "/json/" + JSON_CLIENT_SETTINGS;
            public static final String DEFAULT_JSON_SERVER_SETTINGS = "/json/" + JSON_SERVER_SETTINGS;
        }

        // SQL
        public static final class SQL {
            private static final String SQL_DIR = "/sql/";
            public static final String SQL_CARDS = SQL_DIR + "cards.sql";
        }

        // Images
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_DARK_MODE = IMAGE_DIR + "dark_mode.png";
            public static final String IMAGE_LIGHT_MODE = IMAGE_DIR + "light_mode.png";
            public static final String IMAGE_WUMPUS = IMAGE_DIR + "wumpus.png";
            public static final String IMAGE_NO_WUMPUS = IMAGE_DIR + "no_wumpus.png";
        }
    }
}
