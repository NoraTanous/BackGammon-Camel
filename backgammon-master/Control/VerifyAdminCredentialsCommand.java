package Control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VerifyAdminCredentialsCommand implements Command {
    private boolean authorized;

    @Override
    public void execute() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AdminLogin.fxml"));
            Parent root = loader.load();

            AdminLoginController controller = loader.getController();

            Stage loginStage = new Stage();
            loginStage.setTitle("Admin Login");
            loginStage.setScene(new Scene(root));
            loginStage.showAndWait();

            authorized = controller.isAuthorized();
        } catch (Exception e) {
            e.printStackTrace();
            new ShowAlertCommand("Error", "Unable to verify admin credentials: " + e.getMessage()).execute();
        }
    }

    public boolean isAuthorized() {
        return authorized;
    }
}
