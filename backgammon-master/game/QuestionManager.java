package game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class QuestionManager {
    private List<Map<String, Object>> questions;

    public QuestionManager(InputStream inputStream) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Deserialize directly as a list
            this.questions = mapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            if (this.questions == null || this.questions.isEmpty()) {
                throw new RuntimeException("No questions found in the JSON file.");
            }
            System.out.println("Questions loaded successfully. Total questions: " + questions.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load questions from InputStream.", e);
        }
    }

    public Map<String, Object> getRandomQuestion() {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException("No questions available to select from.");
        }
        return questions.get(new Random().nextInt(questions.size()));
    }
}

