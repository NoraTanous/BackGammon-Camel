package Control;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import Model.GameHistory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryJsonUtil {
    private static final String ABSOLUTE_FILE_PATH = "/backgammon-master/readme-resources/game_history.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // Register JavaTimeModule for LocalDateTime support
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Avoid timestamps
    }

    // Load history from the JSON file using an absolute path
    public static List<GameHistory> loadHistory() {
        try {
            File file = new File(ABSOLUTE_FILE_PATH);
            if (!file.exists()) {
                System.out.println("File not found at path: " + ABSOLUTE_FILE_PATH);
                return new ArrayList<>();
            }
            System.out.println("File found at path: " + ABSOLUTE_FILE_PATH);
            return OBJECT_MAPPER.readValue(file, new TypeReference<List<GameHistory>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save history to the JSON file using an absolute path
    public static void saveHistory(List<GameHistory> historyList) {
        try {
            File file = new File(ABSOLUTE_FILE_PATH);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new IOException("Failed to create parent directories for path: " + file.getAbsolutePath());
            }
            OBJECT_MAPPER.writeValue(file, historyList);
            System.out.println("History saved successfully to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save history to path: " + ABSOLUTE_FILE_PATH);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while saving history.");
            e.printStackTrace();
        }
    }

    public static void saveGameHistory(GameHistory gameHistory) {
        try {
            List<GameHistory> history = loadHistory();
            if (history == null) {
                history = new ArrayList<>();
            }
            history.add(gameHistory);
            saveHistory(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
