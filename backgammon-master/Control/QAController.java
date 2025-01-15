package Control;

import Model.Question;
import Model.QuestionManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QAController {

    @FXML
    private TableView<Question> questionTable;

    @FXML
    private TableColumn<Question, String> questionColumn;

    @FXML
    private TableColumn<Question, String> answersColumn;

    @FXML
    private TableColumn<Question, String> difficultyColumn;

    @FXML
    private TableColumn<Question, Void> actionsColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    private ObservableList<Question> questionList;
    private QuestionManager questionManager;

    public void initialize() {
    	// Get a writable file path for the questions
        Path filePath = QuestionManager.getWritableFilePath();

        // Initialize QuestionManager with the writable path
        questionManager = new QuestionManager(filePath);

        // Load questions into an observable list and set it to the table
        questionList = FXCollections.observableArrayList(questionManager.getQuestions());
        questionTable.setItems(questionList);
        // Set up the columns
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionTextProperty());
        answersColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.join(", ", cellData.getValue().getAnswers())));
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());

        // Set up action buttons (Edit and Delete) in the Actions column
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                // Configure Edit button action
                editButton.setOnAction(event -> editQuestion(getTableRow().getItem()));

                // Configure Delete button action
                deleteButton.setOnAction(event -> deleteQuestion(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionBox = new HBox(editButton, deleteButton);
                    actionBox.setSpacing(10);
                    setGraphic(actionBox);
                }
            }
        });

        // Configure Add button action
        addButton.setOnAction(event -> addQuestion());

        // Configure Back button action
        backButton.setOnAction(event -> backToMenu());
    }

   

    /**
     * Edit an existing question.
     *
     * @param question The question to edit.
     */
    private void addQuestion() {
        // Create a dialog for adding a question
        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle("Add New Question");
        dialog.setHeaderText("Enter the details for the new question:");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create input fields
        TextField questionField = new TextField();
        questionField.setPromptText("Enter the question text");

        TextField correctAnswerField = new TextField();
        correctAnswerField.setPromptText("Enter the correct answer");

        TextField answer1Field = new TextField();
        answer1Field.setPromptText("Enter another answer");

        TextField answer2Field = new TextField();
        answer2Field.setPromptText("Enter another answer");

        TextField answer3Field = new TextField();
        answer3Field.setPromptText("Enter another answer");

        TextField difficultyField = new TextField();
        difficultyField.setPromptText("Enter the difficulty level (1, 2, 3)");

        // Add fields to a VBox
        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(
                new Label("Question:"), questionField,
                new Label("Correct Answer:"), correctAnswerField,
                new Label("Other Answers:"), answer1Field, answer2Field, answer3Field,
                new Label("Difficulty:"), difficultyField
        );

        dialog.getDialogPane().setContent(inputBox);

        // Convert the result to a Question object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    // Validate inputs
                    if (questionField.getText().trim().isEmpty() ||
                            correctAnswerField.getText().trim().isEmpty() ||
                            answer1Field.getText().trim().isEmpty() ||
                            answer2Field.getText().trim().isEmpty() ||
                            answer3Field.getText().trim().isEmpty() ||
                            difficultyField.getText().trim().isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled.");
                    }

                    // Parse inputs
                    String questionText = questionField.getText().trim();
                    String correctAnswer = correctAnswerField.getText().trim();
                    List<String> answers = List.of(
                            correctAnswer,
                            answer1Field.getText().trim(),
                            answer2Field.getText().trim(),
                            answer3Field.getText().trim()
                    );
                    String difficulty = difficultyField.getText().trim();

                    // Return a new Question object
                    return new Question(questionText, answers, correctAnswer, difficulty);
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred");
                    alert.setContentText("Error: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(newQuestion -> {
            // Add the new question to the ObservableList for the UI
            questionList.add(newQuestion);

            // Add the new question to the QuestionManager
            questionManager.addQuestion(newQuestion);

            // Save all changes to the JSON file
            saveQuestionsToJson();

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Question Added");
            successAlert.setContentText("The question has been added successfully.");
            successAlert.showAndWait();

            // Debugging output to confirm the addition
            System.out.println("New question added: " + newQuestion);
            System.out.println("Current question list in manager: " + questionManager.getQuestions());
        });
    }



    public void saveQuestionsToJson() {
        try {
            // Get the writable file path from QuestionManager
            Path filePath = QuestionManager.getWritableFilePath();

            // Convert questions to a list of maps for JSON serialization
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> jsonQuestions = questionManager.getQuestions().stream()
                    .map(q -> Map.of(
                            "question", q.getQuestionText(),
                            "answers", q.getAnswers(),
                            "correct_ans", q.getCorrectAnswer(),
                            "difficulty", q.getDifficulty()
                    ))
                    .toList();

            // Write to the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), jsonQuestions);
            System.out.println("Questions saved to JSON file successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save questions to JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }







    /**
     * Delete a question.
     *
     * @param question The question to delete.
     */
    private void deleteQuestion(Question question) {
        if (question == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Delete Error");
            errorAlert.setHeaderText("No Question Selected");
            errorAlert.setContentText("Please select a question to delete.");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Question");
        confirmDialog.setHeaderText("Are you sure you want to delete this question?");
        confirmDialog.setContentText(question.getQuestionText());

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Remove question from the UI list
                questionList.remove(question);

                // Remove question from the QuestionManager
                questionManager.removeQuestion(question);

                // Save changes to the JSON file
                saveQuestionsToJson();

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Delete Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The question has been deleted successfully.");
                successAlert.showAndWait();
            }
        });
    }


    private void editQuestion(Question question) {
        if (question == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Edit Error");
            errorAlert.setHeaderText("No Question Selected");
            errorAlert.setContentText("Please select a question to edit.");
            errorAlert.showAndWait();
            return;
        }

        // Open a dialog for editing the question
        TextInputDialog questionDialog = new TextInputDialog(question.getQuestionText());
        questionDialog.setTitle("Edit Question");
        questionDialog.setHeaderText("Edit the Question Text");
        questionDialog.setContentText("Question:");
        questionDialog.showAndWait().ifPresent(question::setQuestionText);

        // Edit answers
        List<String> newAnswers = new ArrayList<>();
        for (int i = 0; i < question.getAnswers().size(); i++) {
            TextInputDialog answerDialog = new TextInputDialog(question.getAnswers().get(i));
            answerDialog.setTitle("Edit Answer");
            answerDialog.setHeaderText("Edit Answer " + (i + 1));
            answerDialog.setContentText("Answer:");
            String newAnswer = answerDialog.showAndWait().orElse(null);
            if (newAnswer != null && !newAnswer.isEmpty()) {
                newAnswers.add(newAnswer);
            } else {
                newAnswers.add(question.getAnswers().get(i)); // Keep the old answer if no input
            }
        }
        question.setAnswers(newAnswers);

        // Edit correct answer
        TextInputDialog correctAnswerDialog = new TextInputDialog(question.getCorrectAnswer());
        correctAnswerDialog.setTitle("Edit Correct Answer");
        correctAnswerDialog.setHeaderText("Edit the Correct Answer");
        correctAnswerDialog.setContentText("Correct Answer:");
        correctAnswerDialog.showAndWait().ifPresent(question::setCorrectAnswer);

        // Edit difficulty
        TextInputDialog difficultyDialog = new TextInputDialog(question.getDifficulty());
        difficultyDialog.setTitle("Edit Difficulty");
        difficultyDialog.setHeaderText("Edit Difficulty Level");
        difficultyDialog.setContentText("Difficulty:");
        difficultyDialog.showAndWait().ifPresent(question::setDifficulty);

        // Refresh the TableView to reflect updated data
        questionTable.refresh();

        // Save the updated question list to JSON
        saveQuestionsToJson();

        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Edit Successful");
        successAlert.setHeaderText(null);
        successAlert.setContentText("The question has been updated successfully.");
        successAlert.showAndWait();
    }




    /**
     * Return to the main menu.
     */
    private void backToMenu() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
