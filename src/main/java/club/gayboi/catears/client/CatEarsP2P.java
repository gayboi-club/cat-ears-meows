package club.gayboi.catears.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import club.gayboi.catears.CatEarsConfig;

public class CatEarsP2P {
    private static final char ZWS = '\u200C';
    private static final char DELIM = '\u200B';
    private static final List<String> COLOR_NAMES = Arrays.asList(
        "white", "orange", "magenta", "light_blue", "yellow", "lime",
        "pink", "gray", "light_gray", "cyan", "purple", "blue",
        "brown", "green", "red", "black"
    );

    private static final Map<UUID, P2PData> playerCache = new ConcurrentHashMap<>();

    public record P2PData(boolean earsEnabled, String color, long timestamp) {}

    public static String encodeOutgoing(String message) {
        if (!CatEarsConfig.showEarsLocally) return message;
        int idx = COLOR_NAMES.indexOf(CatEarsConfig.earColor);
        if (idx < 0) idx = 0;
        return message + DELIM + String.valueOf(ZWS).repeat(idx) + DELIM;
    }

    public static void decodeIncoming(String message, UUID sender) {
        int delimStart = message.indexOf(DELIM);
        if (delimStart < 0 || delimStart >= message.length() - 1) return;
        int contentStart = delimStart + 1;
        int delimEnd = message.indexOf(DELIM, contentStart);
        if (delimEnd < contentStart) return;
        String middle = message.substring(contentStart, delimEnd);
        int count = 0;
        for (int i = 0; i < middle.length(); i++) {
            if (middle.charAt(i) == ZWS) count++;
        }
        if (count >= 0 && count < COLOR_NAMES.size()) {
            playerCache.put(sender, new P2PData(true, COLOR_NAMES.get(count), System.currentTimeMillis()));
        }
    }

    public static boolean hasPlayerData(UUID id) {
        P2PData data = playerCache.get(id);
        return data != null && data.earsEnabled();
    }

    public static String getPlayerColor(UUID id) {
        P2PData data = playerCache.get(id);
        return data != null ? data.color() : null;
    }

    public static void removePlayer(UUID id) {
        playerCache.remove(id);
    }

    public static void clearCache() {
        playerCache.clear();
    }
}
