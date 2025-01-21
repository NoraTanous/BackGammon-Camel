package Control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenWindowCommand implements Command {
    private final String fxmlPath;
    private final String title;

    public OpenWindowCommand(String fxmlPath, String title) {
        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    @Override
    public void execute() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if ("/View/QAView.fxml".equals(fxmlPath)) {
                QAController controller = loader.getController();
                controller.setAdminAuthorization(true);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            setCustomIcon(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new ShowAlertCommand("Error", "Unable to load " + title + ": " + e.getMessage()).execute();
        }
    }

    private void setCustomIcon(Stage stage) {
        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/img/backgammon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
        }
    }
}
