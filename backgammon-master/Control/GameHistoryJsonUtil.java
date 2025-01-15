package Control;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import Model.GameHistory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryJsonUtil {
    private static final String FILE_PATH = "readme-resources/game_history.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // Register JavaTimeModule for LocalDateTime support
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    // Load history from the JSON file in the classpath
    public static List<GameHistory> loadHistory() {
        try (InputStream inputStream = GameHistoryJsonUtil.class.getClassLoader().getResourceAsStream(FILE_PATH)) {
            if (inputStream == null) {
                System.out.println("File not found in classpath: " + FILE_PATH);
                return new ArrayList<>();
            }
            System.out.println("File found in classpath: " + FILE_PATH);
            return OBJECT_MAPPER.readValue(inputStream, new TypeReference<List<GameHistory>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveHistory(List<GameHistory> historyList) {
        try {
            // Save to user's home directory for write access
            String userHome = System.getProperty("user.home");
            File file = new File(userHome, "game_history.json");
            System.out.println("Saving history to: " + file.getAbsolutePath());

            OBJECT_MAPPER.writeValue(file, historyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
