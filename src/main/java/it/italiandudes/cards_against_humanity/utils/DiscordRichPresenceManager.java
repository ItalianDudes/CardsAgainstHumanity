package it.italiandudes.cards_against_humanity.utils;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import it.italiandudes.cards_against_humanity.client.utils.ClientSettings;
import org.jetbrains.annotations.Nullable;

public final class DiscordRichPresenceManager {

    // Classes
    public static final class States {
        public static final String MENU = "Nel Menu";
        public static final String SETTINGS = "Modificando le Impostazioni";
        public static final String IN_GAME = "In Gioco";
    }

    // Attributes
    public static final String APPLICATION_ID = "1153595404852658257";
    public static final String APPLICATION_IMAGE = "cards_against_humanity";
    private static DiscordRichPresence presence = null;

    // Rich Presence Initializer
    private static void initializeRichPresence() {
        try {
            if (!ClientSettings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE)) return;
        } catch (Throwable e) {
            return;
        }
        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(APPLICATION_ID, handlers, true, null);
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = APPLICATION_IMAGE;
        lib.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    //noinspection BusyWait
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }
    public static void updateRichPresenceState(@Nullable final String state) {
        try {
            if (!ClientSettings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE)) return;
        } catch (Throwable e) {
            return;
        }
        if (presence == null) initializeRichPresence();
        presence.state = state;
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }
    public static void shutdownRichPresence() {
        presence = null;
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }
}
