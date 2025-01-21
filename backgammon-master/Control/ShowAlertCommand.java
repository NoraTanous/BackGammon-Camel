package Control;

import javafx.scene.control.Alert;

public class ShowAlertCommand implements Command {
    private final String title;
    private final String message;

    public ShowAlertCommand(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public void execute() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
