package Project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
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
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.VBox;
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
	private Image imageShow, imageBirzeit, imageKey;
	private ImageView imageView, imageView2, imageViewKey, imageViewHelp;
	private HBox hbPassword;

	private TextField tfPassword, tfInput, tfSeed, tfKey, tfEmail;
	private PasswordField pfPassword;
	private Button btnEnter, toggleButton, btnFile, btnOk;
	private String correctPassword, enteredPassword;
	private GridPane gp;
	private CheckBox chEmail, chFile;
	private String cipherText, plainText;

	private StringBuilder lineFile;
	private ButtonType encryptionButtonType, decryptionButtonType;
	private Dialog<String> dialog;
	private Optional<String> result;
	private ComboBox<String> modeSelection;

	private AES aes = new AES();
	private AESKeyGenerator generator = new AESKeyGenerator();
	private TextArea taDetails;
	private Label lblKey;
	private String sourceText;
	private byte[] key;

	private int currentIndex = 0;
	private Button nextButton;
	private Button previousButton;
	private ComboBox<Integer> keySize;
	private TextField tfName;
	private VBox vBox;
	private HBox hbName;
	private ImageView imageViewVersion;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Initialize the data structure

		// Initialize Menus and MenuItems
		menuBar = new MenuBar();

		menu2 = new Menu("Help center");
		imageViewHelp = new ImageView(new Image("help-desk.png"));
		imageViewHelp.setFitHeight(30);
		imageViewHelp.setFitWidth(30);
		menu2.setGraphic(imageViewHelp);
		item21 = new MenuItem("About us");
		item22 = new MenuItem("Firas's system");
		menu2.getItems().addAll(item21, item22);

		menu3 = new Menu("Versions");
		imageViewVersion = new ImageView(new Image("version.png"));
		imageViewVersion.setFitHeight(30);
		imageViewVersion.setFitWidth(30);
		menu3.setGraphic(imageViewVersion);
		item31 = new MenuItem("Academic version");
		item32 = new MenuItem("Business version");
		menu3.getItems().addAll(item31, item32);
		item31.setOnAction(e -> academic(primaryStage));
		item32.setOnAction(e -> business(primaryStage));

		menuBar.getMenus().addAll(menu3, menu2);
		menuBar.setPadding(new Insets(10, 10, 10, 10));

		// Labels & Fields Initialization
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
		imageKey = new Image("lock.png");
		imageViewKey = new ImageView(imageKey);
//		imageKey = new Image("lock.png");
//		imageViewKey = new ImageView(imageKey);
		keySize = new ComboBox<>();
		keySize.getItems().addAll(128, 192, 256);
		keySize.setValue(128);
		modeSelection = new ComboBox<>();
		modeSelection.getItems().addAll("ECB", "CBC");
		modeSelection.setValue("ECB"); // Set default value

		// Panes initialization
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

	private void handleEncryption() {
		if (validateInputs()) {
			String seed = tfSeed.getText();
			try {
				key = generator.generateAESKey(seed, keySize.getValue());
				sourceText = chFile.isSelected() ? lineFile.toString() : tfInput.getText();
				cipherText = aes.encrypt(sourceText, key, modeSelection.getValue());
				ta.setText("Cipher Text: " + cipherText.replaceAll("\\s", ""));
				if (chEmail.isSelected()) {
					sendEmail(tfEmail.getText(), "Encryption's output", cipherText);
				}
				showEncryptionDetails(); // Call the method to show encryption details
			} catch (AESException | IllegalArgumentException e) {
				showAlert(e.getMessage(), AlertType.ERROR);
			} catch (Exception e) {
				showAlert(e.getMessage(), AlertType.ERROR);
			}
			System.out.println(AES.toHexString(key));
			tfKey.setText(new String(key, StandardCharsets.UTF_8));
		} else {
			showAlert("Inputs invalid", AlertType.ERROR);
		}
	}

	private void handleDecryption() {
		if (validateInputs()) {
			String seed = tfSeed.getText();
			try {

				key = generator.generateAESKey(seed, keySize.getValue());
				// String decryptedPlaintext = aes.decrypt(readFromFile("CipherText.txt"), key);

				cipherText = chFile.isSelected() ? readFromFile() : tfInput.getText();
				plainText = aes.decrypt(cipherText, key, modeSelection.getValue());
				ta.setText("Decrypted Text: " + plainText);
				if (chEmail.isSelected()) {
					sendEmail(tfEmail.getText(), "Decryption's output", plainText);
				}
			} catch (AESException | IllegalArgumentException e) {
				showAlert(e.getMessage(), AlertType.ERROR);
			} catch (Exception e) {
				showAlert(e.getMessage(), AlertType.ERROR);
			}
			tfKey.setText(new String(key, StandardCharsets.UTF_8));
		} else {
			showAlert("Inputs invalid", AlertType.ERROR);
		}
	}

	private boolean validateInputs() {
		return !(tfSeed.getText().isEmpty() && keySize.getValue() == null) && isNumeric(tfSeed.getText().trim())
				&& (chFile.isSelected() || !tfInput.getText().isEmpty());
	}

	private static String readFromFile() {
		StringBuilder content = new StringBuilder();
		try (Scanner scanner = new Scanner(new File("CipherText.txt"))) {
			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()).append("\n"); // Keep the newlines for each line
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}
		return content.toString().trim(); // Remove the last newline added
	}

	private String showEncryptionDecryptionChoice() {
		dialog = new Dialog<>();
		dialog.setTitle("Choose Mode");
		dialog.setHeaderText("Select the desired mode:");

		encryptionButtonType = new ButtonType("Encryption", ButtonBar.ButtonData.OK_DONE);
		decryptionButtonType = new ButtonType("Decryption", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(encryptionButtonType, decryptionButtonType);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == encryptionButtonType) {
				return "Encryption";
			} else if (dialogButton == decryptionButtonType) {
				return "Decryption";
			}
			return null;
		});

		result = dialog.showAndWait();
		return result.orElse(null);
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
		clearGridPane();

		Label lblInput = new Label("Input:");
		gp.add(lblInput, 0, 0);
		tfInput = new TextField();
		gp.add(tfInput, 1, 0);

		chFile = new CheckBox("Read from a file");
		gp.add(chFile, 2, 0);
		btnFile = new Button("Choose File");
		gp.add(btnFile, 3, 0);
		btnFile.setOnAction(e -> openFileChooser());

		Label lblSeed = new Label("Seed:");
		gp.add(lblSeed, 0, 1);
		tfSeed = new TextField();
		gp.add(tfSeed, 1, 1);
		gp.add(keySize, 2, 1);

		Label lblMode = new Label("Mode:");
		gp.add(lblMode, 0, 2);
		gp.add(modeSelection, 1, 2);

		lblKey = new Label("Key:");
		gp.add(lblKey, 0, 3);
		tfKey = new TextField();
		tfKey.setEditable(false);
		gp.add(tfKey, 1, 3);

		gp.add(taDetails, 0, 4, 4, 1);
		taDetails.clear();

		btnOk = new Button("Process");
		gp.add(btnOk, 0, 5);
		gp.add(previousButton, 2, 5);
		gp.add(nextButton, 3, 5);
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
		gp.add(keySize, 2, 1);
		gp.add(new Label("Mode: "), 0, 3);
		gp.add(modeSelection, 1, 3);
		Label key = new Label("Key");
		key.setGraphic(imageViewKey);
		imageViewKey.setFitWidth(30);
		imageViewKey.setFitHeight(30);
		gp.add(key, 0, 4);
		gp.add(tfKey, 1, 4);
		gp.add(btnOk, 0, 5);
		gp.add(chEmail, 1, 5);
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
		gp.add(keySize, 2, 1);
		gp.add(modeSelection, 1, 3);
		gp.add(new Label("Key"), 0, 4);
		gp.add(tfKey, 1, 4);
		gp.add(btnOk, 0, 5);
		gp.add(chEmail, 1, 5);
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
			if (chEmail.isSelected()) {
				gp.add(lblEmail, 0, 6);
				gp.add(tfEmail, 1, 6);
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
		pfPassword.setPromptText("Enter your password");

		tfPassword.setManaged(false);
		tfPassword.setVisible(false);
		tfPassword.setPromptText("Enter your password");

		toggleButton = new Button("");
		imageView.setFitHeight(20);
		imageView.setFitWidth(20);
		toggleButton.setGraphic(imageView);

		pfPassword.textProperty().bindBidirectional(tfPassword.textProperty());

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

		btnEnter = new Button("Enter");
		btnEnter.setOnAction(e -> checkPassword(primaryStage));
		tfPassword.setOnAction(e -> checkPassword(primaryStage));
		pfPassword.setOnAction(e -> checkPassword(primaryStage));
		tfName = new TextField();
		tfName.setPromptText("Please Enter your name :)");
		hbName = new HBox(10, new Label("User Name"), tfName, new Label("     "));
		hbPassword = new HBox(10, new Label("Password:"), pfPassword, tfPassword, toggleButton, btnEnter);
		hbPassword.setPadding(new Insets(10));
		hbPassword.setAlignment(Pos.CENTER);
		vBox = new VBox(10, hbName, hbPassword);
		vBox.setPadding(new Insets(10));
		vBox.setAlignment(Pos.CENTER);
		hbName.setPadding(new Insets(10));
		hbName.setAlignment(Pos.CENTER);

		scene.setRoot(vBox);

		pfPassword.textProperty().addListener((observable, oldValue, newValue) -> {
			pfPassword.setStyle("");
			tfPassword.setStyle("");
		});
	}

	private void checkPassword(Stage primaryStage) {
		correctPassword = "123"; // Example password
		enteredPassword = pfPassword.getText();

		if (enteredPassword.equals(correctPassword)) {
			mainPage(primaryStage);
		} else {
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

	public static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private void showEncryptionDetails() {

		String[] textSections = {
				"Hey " + tfName.getText()
						+ "!\nI hope my AES program impresses you, press next to start the academic process.",
				"Original Text: " + sourceText,
				"Before we start; AES operates with a 128-bit plain blocks, so because our orginal text exceeds the target size, we're dividing it to blocks and concatating the results afterwards.\nFirst operation in the AES --> Initial transformation for the 128-bit plain text with the cipher key.\n"
						+ toHexString(aes.getInitialState())
						+ "\nThis is the result of XORing the plain text with the cipher key",
				"Second operation we have, is Byte substitution layer, we will we use the left byte for row index and right one for column, Intersection equals a new byte from S-Box\n"
						+ toHexString(aes.getByteSubState()),
				"Third Operation is the Duffusion Layer, one of Shanon terms and each cryptographic system is based on it\nThis layer consists of two subLayers \n1)Shift Row sublayer: "
						+ toHexString(aes.getShiftRowState()) + "\n2)Mix Columns Layer: "
						+ toHexString(aes.getMixColumnsState())
						+ "\nIn shift layer we're left shifting based on row's index\nIn Mix cloumns each byte of cloumn is mapped into a new value that is function of all four bytes in that column",
				"For that we're done with the plain text, these operations are done within one round, and repeated based on key size\n128-bit-->10 Rounds\n192-bit--> 12 Rounds\n256-bit-->14 Rounds \n"
						+ tfName.getText() + " press next to see the key schedule process  \nKey: "
						+ new String(key, StandardCharsets.UTF_8),
				"Now for the key we're doing the following:\nThe key state is divided into 4 words (16 BYTES) XORed and fed into a multiple opertaions like: \n-Rot word: "
						+ toHexString(aes.getRotWordState()) + "\n-SubWord: " + toHexString(aes.getSubWordState())
						+ "\nRcon: " + (aes.getRconState()),
				"Cipher text:" + cipherText.replaceAll("\\s", ""),
				"Our AES academic is done. I hope you've liked\n\nPlease processed to the buisness version to get a real experince of the AES algorithm" };
		taDetails.setText(textSections[0]);
		nextButton.setOnAction(e -> {
			if (currentIndex < textSections.length - 1) {
				currentIndex++;
				taDetails.setText(textSections[currentIndex]);
			}

		});

		previousButton.setOnAction(e -> {
			if (currentIndex > 0) {
				currentIndex--;
				taDetails.setText(textSections[currentIndex]);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
