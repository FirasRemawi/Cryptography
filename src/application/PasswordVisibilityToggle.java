package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PasswordVisibilityToggle extends Application {

    @Override
    public void start(Stage primaryStage) {
        // PasswordField for secure text entry
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // TextField to display password in plain text
        TextField textField = new TextField();
        textField.setManaged(false); // Don't manage size and visibility by the parent
        textField.setVisible(false); // Initially hidden
        textField.setPromptText("Enter your password");

        // Button to toggle visibility
        Button toggleButton = new Button("Show");
        
        // Copy text from PasswordField to TextField to keep them in sync
        passwordField.textProperty().bindBidirectional(textField.textProperty());

        // Toggle visibility on button click
        toggleButton.setOnAction(e -> {
            if (passwordField.isManaged()) {
                // Show plain text; hide password field
                passwordField.setManaged(false);
                passwordField.setVisible(false);
                textField.setManaged(true);
                textField.setVisible(true);
                toggleButton.setText("Hide");
            } else {
                // Show password field; hide plain text
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                textField.setManaged(false);
                textField.setVisible(false);
                toggleButton.setText("Show");
            }
        });

        HBox root = new HBox(10, passwordField, textField, toggleButton);
        root.setPadding(new Insets(10));
        
        Scene scene = new Scene(root, 320, 75);
        primaryStage.setTitle("Password Visibility Toggle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

