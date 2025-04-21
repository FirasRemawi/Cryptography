package Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.control.ButtonBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	private FileChooser fChooser;
	private File f;
	private MenuBar menuBar;
	private Menu menu2, menu3;
	private MenuItem item21, item22, item31, item32;
	private BorderPane bp;
	private TextArea ta;
	private Scene scene;
	private Image imageShow, imageBirzeit;
	private ImageView imageView, imageView2;
	private HBox hbPassword;

	private TextField tfPassword, tfInput, tfSeed, tfKey, tfEmail;
	private PasswordField pfPassword;
	private Button btnEnter, toggleButton, btnFile, btnOk;
	private String correctPassword, enteredPassword;
	private GridPane gp;
	private CheckBox chEmail, chFile;
	private String cipherText;
	private StringBuilder lineFile;
	private ButtonType encryptionButtonType, decryptionButtonType;
	private Dialog<String> dialog;
	private Optional<String> result;
	private DES des = new DES();
	private KeyGenerator generator = new KeyGenerator("");
	private TextArea taDetails;
	private Label lblKey;
	private String key, binKey, permutedKey, sourceText;
	private String[] roundKeys, reversedRoundKeys;
	private String decryptedText;
	private int currentIndex = 0;
	private Button nextButton;
	private Button previousButton;
	private String paddedPlaintext;
	private String hexPlaintext;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Initialize the data structure

		// Initialize Menus and MenuItems
		menuBar = new MenuBar();

		menu2 = new Menu("Help center");
		item21 = new MenuItem("About us");
		item22 = new MenuItem("Firas's system");
		menu2.getItems().addAll(item21, item22);

		menu3 = new Menu("Versions");
		item31 = new MenuItem("Academic version");
		item32 = new MenuItem("Business version");
		menu3.getItems().addAll(item31, item32);
		item31.setOnAction(e -> academic(primaryStage));
		item32.setOnAction(e -> business(primaryStage));

		menuBar.getMenus().addAll(menu3, menu2);
		menuBar.setPadding(new Insets(10, 10, 10, 10));

		// Labels & Fields Initializion
		tfPassword = new TextField();
		pfPassword = new PasswordField();
		tfInput = new TextField();
		tfKey = new TextField();
		tfKey.setEditable(false);
		tfKey.setDisable(true);
		tfEmail = new TextField();
		tfSeed = new TextField();
		btnFile = new Button("Get from file");
		btnOk = new Button("");
		chEmail = new CheckBox("Email");
		chFile = new CheckBox("Read from a file");
		ta = new TextArea();
		ta.setFont(Font.font("Times New Roman", 30));
		ta.setStyle("-fx-font-weight: bold");
		ta.setWrapText(true);
		taDetails = new TextArea();
		taDetails.setFont(Font.font("Times New Roman", 30));
		taDetails.setStyle("-fx-font-weight: bold");
		taDetails.setEditable(false);
		taDetails.setWrapText(true);
		nextButton = new Button("Next");
		previousButton = new Button("Previous");
		imageShow = new Image("showPass.png");
		imageView = new ImageView(imageShow);
		imageBirzeit = new Image("birzeitLogo.png");
		imageView2 = new ImageView(imageBirzeit);

		// Panes intializaion
		hbPassword = new HBox(15);
		hbPassword.getChildren().addAll(new Label("Enter password"), pfPassword);
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(20);
		bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(imageView2);
		scene = new Scene(bp, 720, 600);
		scene.getStylesheets().add(getClass().getResource("/resource/style.css").toExternalForm());

		// Finalize the stage setup
		primaryStage.setScene(scene);
		primaryStage.setTitle("Main Screen");
		loginPage(primaryStage);
		primaryStage.show();
	}

	/**/
	private void academic(Stage primaryStage) {
		bp.setBottom(null);
		scene.setRoot(bp);
		clearGridPane();
		setupAcademicUI(); // Set up UI for decryption
		bp.setCenter(gp);
		ta.setEditable(false);
		clearInputFields();
		ta.clear();
		extracted();
		setupFileChooser();
	}

	/*
	 * In this method we make the user to decide which algorithm he wants to use and
	 * then procced to it
	 */
	private void business(Stage primaryStage) {
		String choice = showEncryptionDecryptionChoice();
		if (choice == null)
			return; // User closed the dialog

		scene.setRoot(bp);
		btnOk.setText(choice); // Set button text to "Encrypt" or "Decrypt" based on choice
		clearGridPane();
		if ("Encryption".equals(choice)) {
			setupGridPaneForEncryption(); // Set up UI for encryption
		} else {
			setupGridPaneForDecryption(); // Set up UI for decryption
		}
		bp.setCenter(gp);
		ta.setEditable(false);
		clearInputFields();
		bp.setBottom(ta);
		ta.clear();
		extracted();
		setupFileChooser();

	}

	/*
	 * In this method, we're taking a source text and a seed from the user, from the
	 * seed we're generating a key key is transformed from HexaDecimel to
	 * binary,then it's permutated and sent to generate round keys to use them in
	 * encryption
	 */
	private void handleEncryption() {
		if (validateInputs()) {
			String seed = tfSeed.getText();
			generator = new KeyGenerator(seed);
			key = generator.generateDESKeyHex(); // Generate a key based on the seed

			binKey = des.hexToBin(key);
			permutedKey = des.permute(binKey, des.getPc1(), 56); // Using PC1 for key permutation
			roundKeys = des.generateRoundKeys(permutedKey);

			// Encrypting the plaintext
			sourceText = chFile.isSelected() ? lineFile.toString() : tfInput.getText();
			// Pad plaintext
			paddedPlaintext = des.padPlaintext(sourceText);

			// Convert padded plaintext to hex
			hexPlaintext = des.stringToHex(paddedPlaintext);
			cipherText = des.encrypt(hexPlaintext, roundKeys);
			ta.setText("Cipher Text: " + cipherText);
			tfKey.setText(key); // Display the key
			writeToFile(cipherText);
			if (chEmail.isSelected()) {
				sendEmail(tfEmail.getText(), "Encryption's output", cipherText);
			}
		} else
			showAlert("Inputs invalid", AlertType.ERROR);
	}

	private void writeToFile(String data) {
		try (FileWriter fileWriter = new FileWriter("CipherText.txt", false);
				PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.println(data);

		} catch (Exception e) {
			showAlert("Failed to write to file: " + e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	private void showEncryptionDetails() {
		// Array to store the text sections
		String[] textSections = { "DES cipher\nAfter receiving the plaintext\n" + sourceText,
				"First operation in DES algorithm is an Intial permutaion for the 64-bit plain text\n"
						+ des.getInitialPermutation(),
				"We start perpering the randomly generated key here is the 64-bit\n" + key
						+ "\nthe 64-bit key is fed into a PC-1 table and outputs a 56-bit\n" + permutedKey
						+ "\n Hex value:\n" + des.binToHex(permutedKey) + "\nSplitted into two halves:\n Left Side "
						+ permutedKey.substring(0, 28) + " " + des.binToHex(permutedKey.substring(0, 28))
						+ "\nRight side: " + permutedKey.substring(28, permutedKey.length()) + " "
						+ des.binToHex(permutedKey.substring(28, permutedKey.length()))
						+ "\nNow we will do left shift for the key,, 1-bit for rounds #1,2,9,16 and rest rounds are shifted by 2-bits and they are fed into a PC-2 to give us a 48-bit round key to be ready for XOR like this:\n "
						+ roundKeys[0] + "\n" + des.binToHex(roundKeys[0]),
				"Right half is expanded from 32-bit to 48-bit like this: " + des.getExpandedRight() + "\n"
						+ des.binToHex(des.getExpandedRight()),
				"Plain is XORed with key to output: " + des.getXored() + "\n" + des.binToHex(des.getXored()),
				"Output is fed into 8-sBoxes to give us a 32-bit " + des.getsBoxed() + "\n"
						+ des.binToHex(des.getsBoxed()),
				"For a final step in the F-function our bits are premuted: " + des.getPermuted() + "\n"
						+ des.binToHex(des.getPermuted()),
				"Now we will XOR and swap halves " + des.getNewRight() + "\n" + des.binToHex(des.getNewRight()),
				"For a last step bits are fed into a final permutaion:\n" + des.getFinalPermutation() + "\n"
						+ des.binToHex(des.getFinalPermutation()),
				"Here is the CipherText: " + cipherText + "\n Hex --> String: " + des.hexToString(cipherText),
				"Rounds are done,the previous steps for a one round but the same steps are repeated for the 16-rounds, For decryption also we'are going to the same steps but we're reversing key"
						+ "\n\n\nResource: Dr.Abuallah Karakra's Stream and Block ciphers slide.\nGod bless your endless efforts, I'm sure some day you'll be proud of your Class A student.\nFiras Remawi.🙏 " };
		taDetails.setText(textSections[0]);
		nextButton.setOnAction(e -> {
			if (currentIndex <= textSections.length - 1) {
				if (currentIndex != textSections.length - 1)
					currentIndex++;
				taDetails.setText(textSections[currentIndex]);

			} else
				currentIndex = textSections.length - 1;
		});

		previousButton.setOnAction(e -> {
			if (currentIndex >= 0) {
				if (currentIndex != 0)
					currentIndex--;
				taDetails.setText(textSections[currentIndex]);

			} else
				currentIndex = 0;
		});
	}

	/*
	 * In this method, we're taking a source text and a seed from the user, from the
	 * seed we're generating a key,key is transformed from HexaDecimel to
	 * binary,then it's permutated and sent to generate round keys, these round
	 * they're used in the encryption process. but DES is symmetric algorithm so we
	 * will reverse the keys and then use them to decrypt
	 */
	private void handleDecryption() {
		if (validateInputs()) {
			String seed = tfSeed.getText();
			generator = new KeyGenerator(seed);
			key = generator.generateDESKeyHex(); // Regenerate the same key from the seed
			binKey = des.hexToBin(key);
			permutedKey = des.permute(binKey, des.getPc1(), 56); // Using PC1 for key permutation
			roundKeys = des.generateRoundKeys(permutedKey);
			// Decryption needs the round keys in reverse order
			reversedRoundKeys = new String[roundKeys.length];
			for (int i = 0; i < roundKeys.length; i++) {
				reversedRoundKeys[i] = roundKeys[roundKeys.length - 1 - i];
			}
			cipherText = chFile.isSelected() ? lineFile.toString() : tfInput.getText();
			decryptedText = des.decrypt(cipherText.trim(), reversedRoundKeys);
			// Convert decrypted hex back to padded plaintext
			String paddedDecryptedText = des.hexToString(decryptedText);
			// Remove padding from decrypted text
			decryptedText = des.removePadding(paddedDecryptedText);
			ta.setText("Decrypted Text: " + decryptedText);
			tfKey.setText(key); // Display the key
			if (chEmail.isSelected()) {
				sendEmail(tfEmail.getText(), "Decryption's output", decryptedText);
			}
		} else
			showAlert("Inputs invalid", AlertType.ERROR);
	}

	private boolean validateInputs() {
		return !tfSeed.getText().isEmpty() && isNumeric(tfSeed.getText().trim())
				&& (chFile.isSelected() || !tfInput.getText().isEmpty());
	}

	private String showEncryptionDecryptionChoice() {
		// Create the custom dialog.
		dialog = new Dialog<>();
		dialog.setTitle("Choose Mode");
		dialog.setHeaderText("Select the desired mode:");

		// Set the button types.
		encryptionButtonType = new ButtonType("Encryption", ButtonBar.ButtonData.OK_DONE);
		decryptionButtonType = new ButtonType("Decryption", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(encryptionButtonType, decryptionButtonType);

		// Handle the result.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == encryptionButtonType) {
				return "Encryption";
			} else if (dialogButton == decryptionButtonType) {
				return "Decryption";
			}
			return null; // No selection
		});

		result = dialog.showAndWait();
		return result.orElse(null); // This will be 'Encryption' or 'Decryption' or null if closed
	}

	private void setupFileChooser() {
		gp.getChildren().remove(btnFile);
		chFile.setOnAction(e -> {
			if (chFile.isSelected()) {
				tfInput.setDisable(true);
				gp.add(btnFile, 3, 0);
				btnFile.setOnAction(e1 -> openFileChooser());
			} else {
				tfInput.setDisable(false);
				gp.getChildren().removeAll(btnFile);
				btnOk.setText("Encrypt");
			}
		});
	}

	private void openFileChooser() {
		fChooser = new FileChooser();
		fChooser.setInitialDirectory(new File("C:\\Users\\ASUS\\eclipse-workspace\\Cryptography"));
		f = fChooser.showOpenDialog(null);
		if (f != null) {
			try (Scanner scanner = new Scanner(f)) {
				lineFile = new StringBuilder();
				while (scanner.hasNextLine()) {
					lineFile.append(scanner.nextLine()).append("\n");
				}
			} catch (FileNotFoundException e) {
				showAlert("File not found: " + e.getMessage(), Alert.AlertType.ERROR);
			}
		}
	}

	private void clearGridPane() {
		if (gp.getChildren() != null) {
			gp.getChildren().clear();
		}
	}

	private void setupAcademicUI() {
		clearGridPane(); // Clear previous settings

		// Input
		Label lblInput = new Label("Input:");
		gp.add(lblInput, 0, 0);
		tfInput = new TextField();
		gp.add(tfInput, 1, 0);

		// Read from file
		chFile = new CheckBox("Read from a file");
		gp.add(chFile, 2, 0);
		btnFile = new Button("Choose File");
		gp.add(btnFile, 3, 0);
		btnFile.setOnAction(e -> openFileChooser());

		// Seed
		Label lblSeed = new Label("Seed:");
		gp.add(lblSeed, 0, 1);
		tfSeed = new TextField();
		gp.add(tfSeed, 1, 1);

		// Key Display
		lblKey = new Label("Key:");
		gp.add(lblKey, 0, 2);
		tfKey = new TextField();
		tfKey.setEditable(false);
		gp.add(tfKey, 1, 2);

		gp.add(taDetails, 0, 3, 4, 1);
		taDetails.clear();

		// Encrypt Button
		btnOk = new Button("Process");
		gp.add(btnOk, 0, 4);
		gp.add(previousButton, 2, 4);
		gp.add(nextButton, 3, 4);
		previousButton.setDisable(true);
		nextButton.setDisable(true);

		btnOk.setOnAction(e -> {
			if (!validateInputs()) {
				showAlert("Please fill all needed inputs", AlertType.ERROR);
				return;
			}
			previousButton.setDisable(false);
			nextButton.setDisable(false);
			String choice = showEncryptionDecryptionChoice();
			if (choice == null)
				return;
			else if (choice.equalsIgnoreCase("Encryption")) {
				handleEncryption();
				showEncryptionDetails();

			} else {
				handleDecryption();

			}

		});

		// Configure file chooser
		chFile.setOnAction(e -> {
			tfInput.setDisable(chFile.isSelected());
			btnFile.setDisable(!chFile.isSelected());
		});
	}

	private void setupGridPaneForEncryption() {
		gp.add(new Label("Input"), 0, 0);
		gp.add(tfInput, 1, 0);
		gp.add(chFile, 2, 0);
		gp.add(new Label("Seed"), 0, 1);
		gp.add(tfSeed, 1, 1);
		gp.add(new Label("Key"), 0, 3);
		gp.add(tfKey, 1, 3);
		gp.add(btnOk, 0, 4);
		gp.add(chEmail, 1, 4);
		if (chEmail.isSelected())
			chEmail.setSelected(false);
		btnOk.setOnAction(e -> handleEncryption());

	}

	private void setupGridPaneForDecryption() {
		gp.add(new Label("Input"), 0, 0);
		gp.add(tfInput, 1, 0);
		gp.add(chFile, 2, 0);
		gp.add(new Label("Seed"), 0, 1);
		gp.add(tfSeed, 1, 1);
		gp.add(new Label("Key"), 0, 3);
		gp.add(tfKey, 1, 3);
		gp.add(btnOk, 0, 4);
		gp.add(chEmail, 1, 4);
		if (chEmail.isSelected())
			chEmail.setSelected(false);
		btnOk.setOnAction(e -> handleDecryption());

	}

	private void sendEmail(String to, String subject, String content) {
		EmailService emailService = new EmailService();
		emailService.sendEmail(to, subject, content);
	}

	private void extracted() {
		Label lblEmail = new Label("Recipient's Email");
		chEmail.setOnAction(e1 -> {
			// Add email input fields dynamically based on checkbox selection
			if (chEmail.isSelected()) {
				gp.add(lblEmail, 0, 5);
				gp.add(tfEmail, 1, 5);
				btnOk.setText("Send Email");
			} else {
				gp.getChildren().removeAll(lblEmail, tfEmail);
				btnOk.setText("Show");
			}
		});
	}

	private void clearInputFields() {
		tfInput.setText("");
		tfKey.setText("");
		tfSeed.setText("");
		ta.clear();
		chEmail.setSelected(false);
		chEmail.setWrapText(true);
	}

	private void loginPage(Stage primaryStage) {

		// Password field setup
		pfPassword.setPromptText("Enter your password");

		// TextField setup for showing/hiding password
		tfPassword.setManaged(false);
		tfPassword.setVisible(false);
		tfPassword.setPromptText("Enter your password");

		// Toggle button setup with image
		toggleButton = new Button("");
		imageView.setFitHeight(20);
		imageView.setFitWidth(20);
		toggleButton.setGraphic(imageView);

		// Sync text fields
		pfPassword.textProperty().bindBidirectional(tfPassword.textProperty());

		// Toggle visibility on button click
		toggleButton.setOnAction(e -> {
			if (pfPassword.isManaged()) {
				pfPassword.setManaged(false);
				pfPassword.setVisible(false);
				tfPassword.setManaged(true);
				tfPassword.setVisible(true);
				imageView.setImage(imageShow);

			} else {
				pfPassword.setManaged(true);
				pfPassword.setVisible(true);
				tfPassword.setManaged(false);
				tfPassword.setVisible(false);
				imageView.setImage(imageShow);
			}
		});

		// Enter button setup
		btnEnter = new Button("Enter");
		btnEnter.setOnAction(e -> checkPassword(primaryStage));
		tfPassword.setOnAction(e -> checkPassword(primaryStage));
		pfPassword.setOnAction(e -> checkPassword(primaryStage));

		// Layout setup
		hbPassword = new HBox(10, new Label("Password:"), pfPassword, tfPassword, toggleButton, btnEnter);
		hbPassword.setPadding(new Insets(10));
		hbPassword.setAlignment(Pos.CENTER);
		scene.setRoot(hbPassword);
		// Change listener for resetting the style when the user modifies the password
		pfPassword.textProperty().addListener((observable, oldValue, newValue) -> {
			pfPassword.setStyle(""); // Clears any styling applied
			tfPassword.setStyle("");
		});
	}

	private void checkPassword(Stage primaryStage) {
		correctPassword = "123"; // Example password
		enteredPassword = pfPassword.getText();

		if (enteredPassword.equals(correctPassword)) {
			// Password is correct; move to the next scene
			mainPage(primaryStage);
		} else {
			// Password is incorrect; update UI
			pfPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
			tfPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
		}
	}

	private void showAlert(String message, Alert.AlertType type) {
		Alert alert = new Alert(type, message, ButtonType.OK);
		alert.showAndWait();
	}

	private static boolean isNumeric(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void mainPage(Stage primaryStage) {
		scene.setRoot(bp);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
