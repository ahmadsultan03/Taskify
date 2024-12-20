import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutWindow {
    public AboutWindow() {
        Stage aboutStage = new Stage();
        aboutStage.setTitle("About - Taskify");

        // About information layout with gradient background
        VBox aboutLayout = new VBox(15);
        aboutLayout.setPadding(new javafx.geometry.Insets(20));

        // Set a bluish-purplish gradient background
        aboutLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");

        // Label for Taskify header
        Label headerLabel = new Label("Taskify - Track. Plan. Achieve.");
        headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 10px 0;");

        // Label for description with improved visibility
        Label descriptionLabel = new Label("Taskify helps users manage tasks by priority.\n It allows adding, updating, and deleting\n tasks to stay organized and productive.");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px 0;");

        // Label for Developers section
        Label developersLabel = new Label("Developers:");
        developersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5px 0;");

        // Developers list as bullet points
        Label developersListLabel = new Label("• Muhammad Ahmad Sultan\n• Shandana Iftikhar\n• Samar Qaiser");
        developersListLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 5px 0;");

        // Adding all labels to the VBox layout
        aboutLayout.getChildren().addAll(headerLabel, descriptionLabel, developersLabel, developersListLabel);

        Scene aboutScene = new Scene(aboutLayout, 400, 350);
        aboutStage.setScene(aboutScene);
        aboutStage.show();
    }
}