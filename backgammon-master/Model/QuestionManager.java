package Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestionManager {
    private final List<Question> questions;

    public QuestionManager(Path filePath) {
        try {
            // Check if the writable file exists
            if (!Files.exists(filePath)) {
                // Ensure the directory exists
                Files.createDirectories(filePath.getParent());

                // Copy the resource file to the writable location
                try (InputStream resourceStream = getClass().getResourceAsStream("/readme-resources/CamelQuestionDB.json")) {
                    if (resourceStream == null) {
                        throw new RuntimeException("Default resource file not found!");
                    }
                    Files.copy(resourceStream, filePath);
                    System.out.println("Default file copied to: " + filePath.toAbsolutePath());
                }
            }

            // Load questions from the writable file
            ObjectMapper mapper = new ObjectMapper();
            questions = mapper.readValue(filePath.toFile(), new TypeReference<List<Question>>() {});
            if (questions == null || questions.isEmpty()) {
                throw new RuntimeException("No questions found in the JSON file.");
            }
            System.out.println("Questions loaded successfully. Total questions: " + questions.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load questions from JSON file.", e);
        }
    }
    public QuestionManager(InputStream inputStream) {
        try {
            // Use Jackson ObjectMapper to parse JSON from the InputStream
            ObjectMapper mapper = new ObjectMapper();
            List<Question> loadedQuestions = mapper.readValue(inputStream, new TypeReference<List<Question>>() {});

            if (loadedQuestions == null || loadedQuestions.isEmpty()) {
                throw new RuntimeException("No questions found in the JSON stream.");
            }
            this.questions = new ArrayList<>(loadedQuestions);
            System.out.println("Questions loaded successfully. Total questions: " + questions.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load questions from InputStream.", e);
        }
    }



    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public void addQuestion(Question question) {
        questions.add(question); // Add the Question object directly to the list
    }

    public void addQuestion(Map<String, Object> questionData) {
        String questionText = (String) questionData.get("question");
        List<String> answers = (List<String>) questionData.get("answers");
        String correctAnswer = (String) questionData.get("correct_ans");
        String difficulty = (String) questionData.get("difficulty");

        Question question = new Question(questionText, answers, correctAnswer, difficulty);
        addQuestion(question);
    }


    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public void updateQuestion(Question oldQuestion, Question newQuestion) {
        int index = questions.indexOf(oldQuestion);
        if (index != -1) {
            questions.set(index, newQuestion);
        } else {
            throw new IllegalArgumentException("Question not found in the list.");
        }
    }

    public Question getRandomQuestion(String diff) {
        // Filter questions by difficulty
        List<Question> filteredQuestions = questions.stream()
                .filter(q -> q.getDifficulty().equalsIgnoreCase(diff))
                .collect(Collectors.toList());

        if (filteredQuestions.isEmpty()) {
            return null; // No questions found for the given difficulty
        }

        // Select a random question
        Random random = new Random();
        return filteredQuestions.get(random.nextInt(filteredQuestions.size()));
    }
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions available.");
        }
        return questions.get(new Random().nextInt(questions.size()));
    }

    public void saveQuestionsToJson() {
        try {
            Path filePath = getWritableFilePath(); // Use the writable file path
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), questions);
            System.out.println("Questions saved to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save questions to JSON file", e);
        }
    }

    public static Path getWritableFilePath() {
        // Save file in a hidden folder in the user's home directory for portability
        Path filePath = Paths.get(System.getProperty("user.home"), "readme-resources", "CamelQuestionDB.json");

        try {
            // Ensure the parent directory exists
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for writable path: " + filePath, e);
        }

        return filePath;
    }



}
