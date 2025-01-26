package Control;

import Model.GameHistory;
import Model.Question;
import Model.QuestionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class Tests {

    private QuestionManager questionManager;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        // Path to the existing CamelQuestionDB.json in the resources or home directory
        Path filePath = Paths.get(System.getProperty("user.dir"), "readme-resources", "CamelQuestionDB2.json");

        // Ensure the file exists
        if (!Files.exists(filePath)) {
            throw new IllegalStateException("The CamelQuestionDB.json file is missing at: " + filePath);
        }

        // Initialize QuestionManager with the existing file
        questionManager = new QuestionManager(filePath);

        // Clean up any previous test files for GameHistory
        String userHome = System.getProperty("user.home");
        Files.deleteIfExists(Paths.get(userHome, "game_history.json"));
    }

    // QuestionManager Tests
   

    @Test
    public void testAddQuestion() {
        // Define the new question
        Map<String, Object> newQuestion = Map.of(
                "question", "What is 5 + 5?",
                "answers", List.of("8", "10", "12", "15"),
                "correct_ans", "10",
                "difficulty", "Medium"
        );

        // Add the new question
        questionManager.addQuestion(newQuestion);

        // Save the updated list back to the JSON file
        questionManager.saveQuestionsToJson();

        // Reload the questions from the JSON file to verify the addition
        questionManager = new QuestionManager(QuestionManager.getWritableFilePath());

        // Assert that the new question is in the list
        List<Question> questions = questionManager.getQuestions();
        assertTrue("Should contain the added question", 
            questions.stream().anyMatch(q -> q.getQuestionText().equals("What is 5 + 5?")));
    }


   

    @Test
    public void testUpdateQuestion() {
        Question oldQuestion = questionManager.getQuestions().get(0);
        Question newQuestion = new Question(
                "What is 10 + 10?",
                List.of("15", "20", "25", "30"),
                "20",
                "Medium"
        );

        questionManager.updateQuestion(oldQuestion, newQuestion);

        Question updatedQuestion = questionManager.getQuestions().get(0);
        assertEquals("What is 10 + 10?", updatedQuestion.getQuestionText());
        assertEquals("20", updatedQuestion.getCorrectAnswer());
    }

    @Test
    public void testGetRandomQuestion() {
        Question randomQuestion = questionManager.getRandomQuestion();
        assertNotNull("Random question should not be null", randomQuestion);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRandomQuestionWithNoQuestions() {
        // Remove all questions
        questionManager.getQuestions().forEach(questionManager::removeQuestion);

        // Attempt to get a random question
        questionManager.getRandomQuestion();
    }

    // GameHistoryJsonUtil Tests
    @Test
    public void testLoadHistory_ValidFile() throws Exception {
        String tempJson = "[{\"playerOne\":\"Player1\",\"playerTwo\":\"Player2\",\"scorePlayerOne\":5,\"scorePlayerTwo\":3,\"winner\":\"Player1\",\"dateTime\":\"2024-01-01T12:00:00\"}]";
        File tempFile = new File(GameHistoryJsonUtil.class.getClassLoader().getResource("readme-resources/game_history.json").toURI());
        Files.write(tempFile.toPath(), tempJson.getBytes());

        List<GameHistory> history = GameHistoryJsonUtil.loadHistory();

        assertEquals(1, history.size());
        assertEquals("Player1", history.get(0).getPlayerOne());
        assertEquals(5, history.get(0).getScorePlayerOne());
    }
}
