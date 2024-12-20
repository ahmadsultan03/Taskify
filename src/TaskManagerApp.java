import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerApp extends Application {
    private List<Task> tasks = new ArrayList<>();
    private ListView<String> highPriorityListView = new ListView<>();
    private ListView<String> mediumPriorityListView = new ListView<>();
    private ListView<String> lowPriorityListView = new ListView<>();
    private TextField taskNameField = new TextField();
    private ComboBox<String> priorityCombo = new ComboBox<>();
    private Button addButton = new Button("Add Task");
    private Button updateButton = new Button("Update Task");
    private Button deleteButton = new Button("Delete Task");
    private Button aboutButton = new Button("About");
    private Button exitButton = new Button("Exit");
    private int selectedIndex = -1;
    private String selectedPriority = null;

    @Override
    public void start(Stage primaryStage) {
        // Set up the priority combo box
        priorityCombo.getItems().addAll("High", "Medium", "Low");

        // Set up buttons
        addButton.setOnAction(e -> addTask());
        updateButton.setOnAction(e -> updateTask());
        deleteButton.setOnAction(e -> deleteTask());
        aboutButton.setOnAction(e -> new AboutWindow());
        exitButton.setOnAction(e -> new ExitWindow(primaryStage));

        // Add selection listeners to the ListView
        addSelectionListener(highPriorityListView, "High");
        addSelectionListener(mediumPriorityListView, "Medium");
        addSelectionListener(lowPriorityListView, "Low");

        // Layout for priority views
        VBox highPriorityLayout = new VBox(5, addWhiteTextLabel("High Priority:"), highPriorityListView);
        VBox mediumPriorityLayout = new VBox(5, addWhiteTextLabel("Medium Priority:"), mediumPriorityListView);
        VBox lowPriorityLayout = new VBox(5, addWhiteTextLabel("Low Priority:"), lowPriorityListView);

        // Set up input and button layout
        HBox inputLayout = new HBox(10, addWhiteTextLabel("Task Name:"), taskNameField, addWhiteTextLabel("Priority:"), priorityCombo);
        HBox buttonLayout = new HBox(10, addButton, updateButton, deleteButton, aboutButton, exitButton);

        // Main layout
        VBox mainLayout = new VBox(10, inputLayout, buttonLayout, highPriorityLayout, mediumPriorityLayout, lowPriorityLayout);
        mainLayout.setSpacing(15);
        mainLayout.setPadding(new javafx.geometry.Insets(10));

        // Apply background
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");
        
        //        mainLayout.setStyle("-fx-background-image: url('file:background.jpg');" +
       //                "-fx-background-size: cover;");

        // Styling for buttons
        styleButton(addButton, "#007bff", "#0056b3");
        styleButton(updateButton, "#953553", "#673147");
        styleButton(deleteButton, "#dc3545", "#c82333");
        styleButton(aboutButton, "#545454", "#3f3f3f");
        styleButton(exitButton, "#808080", "#6a6a6a");

        // Set up the scene
        Scene scene = new Scene(mainLayout, 600, 500);

        // Set scene and show
        primaryStage.setTitle("Taskify - Track. Plan. Achieve.");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create a white text label
    private Label addWhiteTextLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        return label;
    }

    // Helper method to style buttons
    private void styleButton(Button button, String normalColor, String hoverColor) {
        button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;"));
    }

    private void addTask() {
        String name = taskNameField.getText();
        String priority = priorityCombo.getValue();

        if (name.isEmpty() || priority == null) {
            showAlert("Error", "Task name and priority must be filled!", Alert.AlertType.ERROR);
            return;
        }

        tasks.add(new Task(name, priority));
        refreshTaskListViews();
        clearInputs();
    }

    private void updateTask() {
        if (selectedIndex < 0 || selectedPriority == null) {
            showAlert("Error", "No task selected to update!", Alert.AlertType.ERROR);
            return;
        }

        String name = taskNameField.getText();
        String priority = priorityCombo.getValue();

        if (name.isEmpty() || priority == null) {
            showAlert("Error", "Task name and priority must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Update the selected task
        Task task = tasks.get(selectedIndex);
        task.setName(name);
        task.setPriority(priority);

        refreshTaskListViews();
        clearInputs();
    }

    private void deleteTask() {
        if (selectedIndex < 0 || selectedPriority == null) {
            showAlert("Error", "No task selected to delete!", Alert.AlertType.ERROR);
            return;
        }

        // Remove the selected task
        tasks.remove(selectedIndex);

        refreshTaskListViews();
        clearInputs();
    }

    private void addSelectionListener(ListView<String> listView, String priority) {
        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
                selectedIndex = getTaskIndexFromListView(priority, newVal.intValue());
                selectedPriority = priority;

                // Load selected task details into the input fields
                if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
                    Task selectedTask = tasks.get(selectedIndex);
                    taskNameField.setText(selectedTask.getName());
                    priorityCombo.setValue(selectedTask.getPriority());
                }
            }
        });
    }

    private int getTaskIndexFromListView(String priority, int listIndex) {
        int index = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getPriority().equals(priority)) {
                if (index == listIndex) {
                    return i;
                }
                index++;
            }
        }
        return -1; // Should not happen
    }

    private void refreshTaskListViews() {
        // Clear all priority lists
        highPriorityListView.getItems().clear();
        mediumPriorityListView.getItems().clear();
        lowPriorityListView.getItems().clear();

        // Add tasks to appropriate priority list
        for (Task task : tasks) {
            String priority = task.getPriority();
            if ("High".equals(priority)) {
                highPriorityListView.getItems().add(task.toString());
            } else if ("Medium".equals(priority)) {
                mediumPriorityListView.getItems().add(task.toString());
            } else if ("Low".equals(priority)) {
                lowPriorityListView.getItems().add(task.toString());
            }
        }
    }

    private void clearInputs() {
        taskNameField.clear();
        priorityCombo.getSelectionModel().clearSelection();
        selectedIndex = -1;
        selectedPriority = null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Task {
    private String name;
    private String priority;

    public Task(String name, String priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return name;
    }
}
