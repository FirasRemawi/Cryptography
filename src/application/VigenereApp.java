package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VigenereApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Vigenère Cipher");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		TextField txtPlaintext = new TextField();
		TextField txtKey = new TextField();
		RadioButton rbAutoKey = new RadioButton("Use Auto Key");
		RadioButton rbStandardKey = new RadioButton("Use Standard Key");
		ToggleGroup keyTypeGroup = new ToggleGroup();
		rbAutoKey.setToggleGroup(keyTypeGroup);
		rbStandardKey.setToggleGroup(keyTypeGroup);
		rbStandardKey.setSelected(true);
		Button btnEncrypt = new Button("Encrypt");
		Button btnDecrypt = new Button("Decrypt");
		Button btnRandomKey = new Button("Generate Random Key");
		TextArea txtResult = new TextArea();
		txtResult.setEditable(false);

		grid.add(new Label("Plaintext/Ciphertext:"), 0, 0);
		grid.add(txtPlaintext, 1, 0);
		grid.add(new Label("Key:"), 0, 1);
		grid.add(txtKey, 1, 1);
		Button btnFile = new Button("get from File");
		grid.add(btnFile, 2, 0);
		grid.add(rbAutoKey, 1, 2);
		grid.add(rbStandardKey, 1, 3);
		grid.add(btnEncrypt, 0, 4);
		grid.add(btnDecrypt, 1, 4);
		grid.add(btnRandomKey, 2, 1);
		grid.add(txtResult, 0, 5, 3, 1);

		btnEncrypt.setOnAction(e -> {
			boolean useAutoKey = rbAutoKey.isSelected();
			String encryptedText = VigenereCipher.encryptText(txtPlaintext.getText(), txtKey.getText(), useAutoKey);
			txtResult.setText(encryptedText);
		});

		btnDecrypt.setOnAction(e -> {
			boolean useAutoKey = rbAutoKey.isSelected();
			String decryptedText = VigenereCipher.decryptText(txtPlaintext.getText(), txtKey.getText(), useAutoKey);
			txtResult.setText(decryptedText);
		});

		btnRandomKey.setOnAction(e -> {
			String randomKey = VigenereCipher.generateRandomKey(10); // Example length of 10
			txtKey.setText(randomKey);
		});

		Scene scene = new Scene(grid, 500, 275);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
