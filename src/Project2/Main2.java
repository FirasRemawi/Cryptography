package Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Year;
import java.util.Base64;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main2 extends Application {
	private FileChooser fChooser;
	private File f;
	private MenuBar menuBar, menuBarOperations;
	private Menu menu1, menu2, menu3, menuHill, menuPlayfair, menuOTP;
	private MenuItem item1, item21, item22, item3, hillEncrypt, hillDecrypt, playFairEncrypt, playFairDecrypt,
			otpEncrypt, otpDecrypt;
	private BorderPane bp;
	private TextArea ta;
	private ToggleGroup tg;
	private Scene scene;
	private Image imageShow, imageHide, imageBirzeit;
	private ImageView imageView;
	private HBox hbPassword;

	private TextField tfPassword, tfInput, tfSeed, tfKey, tfEmail;
	private PasswordField pfPassword;
	private Button btnEnter, toggleButton, btnFile, btnOk;
	private String correctPassword, enteredPassword;
	private Hyperlink link;
	private GridPane gp;
	private CheckBox chEmail, chFile;
	private String cipherText;
	private Alert alert;
	private int[][] matrix;
	private String plainText;
	private StringBuilder lineFile;
	private StringBuilder cipherTxt;

	public void start(Stage primaryStage) throws Exception {
		// Initialize the data structure

		// Initialize Menus and MenuItems
		menuBar = new MenuBar();
		menuBarOperations = new MenuBar();
		menu1 = new Menu("File");
		item1 = new MenuItem("Open");
		menu1.getItems().add(item1);

		menu2 = new Menu("Help center");
		item21 = new MenuItem("About us");
		item22 = new MenuItem("Firas's system");
		menu2.getItems().addAll(item21, item22);

		menu3 = new Menu("Operations");
		item3 = new MenuItem("proceed");
		menu3.getItems().add(item3);

		menuBar.getMenus().addAll(menu1, menu3, menu2);
		menuBar.setPadding(new Insets(10, 10, 10, 10));

		menuHill = new Menu("Hill cipher");
		hillEncrypt = new MenuItem("Encrypt");
		hillEncrypt.setOnAction(e -> setupHillEncryptionUI(primaryStage));
		hillDecrypt = new MenuItem("Decrypt");
		menuHill.getItems().addAll(hillEncrypt, hillDecrypt);

		menuPlayfair = new Menu("PlayFair cipher");
		playFairEncrypt = new MenuItem("Encrypt");
		playFairDecrypt = new MenuItem("Decrypt");
		menuPlayfair.getItems().addAll(playFairEncrypt, playFairDecrypt);

		menuOTP = new Menu("One Time Pad cipher");
		otpEncrypt = new MenuItem("Encrypt");
		otpDecrypt = new MenuItem("Decrypt");
		menuOTP.getItems().addAll(otpEncrypt, otpDecrypt);

		menuBarOperations.getMenus().addAll(menuHill, menuPlayfair, menuOTP);
		menuBarOperations.setPadding(new Insets(10, 10, 10, 10));

		// Image & ImageView Initializion
		imageShow = new Image("showPassword.png");
		imageHide = new Image("hidePassword.jpeg");
		imageBirzeit = new Image("birzeitLogo.png");
		imageView = new ImageView(imageShow);

		// Labels & Fields Initializion
		tfPassword = new TextField();
		pfPassword = new PasswordField();
		tfInput = new TextField();
		tfKey = new TextField();
		tfKey.setEditable(false);
		tfKey.setDisable(true);
		tfEmail = new TextField();
		tfSeed = new TextField();
		link = new Hyperlink("");
		btnFile = new Button("Get from file");
		btnOk = new Button("");
		chEmail = new CheckBox("Email");
		chFile = new CheckBox("Read from a file");
		ta = new TextArea();
		ta.setFont(Font.font("Times New Roman", 30));
		ta.setStyle("-fx-font-weight: bold");
		ta.setWrapText(true);

		// Panes intializaion
		hbPassword = new HBox(15);
		hbPassword.getChildren().addAll(new Label("Enter password"), pfPassword);
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(20);
		bp = new BorderPane();
		bp.setTop(menuBar);
		scene = new Scene(bp, 720, 600);
		scene.getStylesheets().add(getClass().getResource("/resource/style.css").toExternalForm());

		item3.setOnAction(e -> operationsMenu(primaryStage));

		// Finalize the stage setup
		primaryStage.setScene(scene);
		primaryStage.setTitle("Main Screen");
		loginPage(primaryStage);
		primaryStage.show();

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
	}

	private String readFromFile() {
		lineFile.setLength(0);
		try (Scanner scanner = new Scanner(new File("CipherText.txt"))) {
			while (scanner.hasNext()) {
				lineFile.append(scanner.next());
			}
		} catch (FileNotFoundException e) {
			showAlert("File not found: " + e.getMessage(), Alert.AlertType.ERROR);
		}
		return lineFile.toString();
	}

	private void updateUIPostDecryption() {
		if (chEmail.isSelected() && !tfEmail.getText().isEmpty()) {
			sendEmail(tfEmail.getText(), "Decryption's output", cipherText);
		} else if (chEmail.isSelected()) {
			showAlert("Please provide a recipient's email.", Alert.AlertType.ERROR);
		}
	}

	private void extracted() {
		chEmail.setOnAction(e1 -> {
			// Add email input fields dynamically based on checkbox selection
			if (chEmail.isSelected()) {
				gp.add(new Label("Recipient's Email"), 0, 5);
				gp.add(tfEmail, 1, 5);
				btnOk.setText("Send Email");
			} else {
				gp.getChildren().removeAll(new Label("Recipient's Email"), tfEmail);
				btnOk.setText("Show");
			}
		});
	}

	private void setupHillEncryptionUI(Stage primaryStage) {
		scene.setRoot(bp);
		btnOk.setText("Encrypt");
		clearGridPane();
		setupGridPaneForEncryption();
		bp.setCenter(gp);
		ta.setEditable(false);
		clearInputFields();
		bp.setBottom(ta);
		extracted();
		setupFileChooser();
	}

	private void clearGridPane() {
		if (gp.getChildren() != null) {
			gp.getChildren().clear();
		}
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
	}

	private void clearInputFields() {
		tfInput.setText("");
		tfKey.setText("");
		tfSeed.setText("");
	}

	private boolean validateInputs() {
		return !tfSeed.getText().isEmpty() && isNumeric(tfSeed.getText().trim())
				&& (chFile.isSelected() || !tfInput.getText().isEmpty());
	}

	private void writeToFile(String data) {
		try (FileWriter fileWriter = new FileWriter("CipherText.txt", false);
				PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.println(data);
		} catch (Exception e) {
			showAlert("Failed to write to file: " + e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	private void updateUIPostEncryption() {
		if (chEmail.isSelected() && !tfEmail.getText().isEmpty()) {
			sendEmail(tfEmail.getText(), "Encryption's output", cipherText);
		} else if (chEmail.isSelected()) {
			showAlert("Please provide a recipient's email.", Alert.AlertType.ERROR);
		}
	}

	private void sendEmail(String to, String subject, String content) {
		EmailService emailService = new EmailService();
		emailService.sendEmail(to, subject, content);
	}

	private void showAlert(String message, Alert.AlertType type) {
		Alert alert = new Alert(type, message, ButtonType.OK);
		alert.showAndWait();
	}

	private void setupFileChooser() {
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

	private void operationsMenu(Stage primaryStage) {
		scene.setRoot(bp);
		bp.setTop(menuBarOperations);

	}

	private void mainPage(Stage primaryStage) {
		scene.setRoot(bp);
		imageView.setImage(imageBirzeit);
		imageView.setFitHeight(350);
		imageView.setFitWidth(550);
		bp.setCenter(imageView);

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
				imageView.setImage(imageHide);
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

	private static boolean isNumeric(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception ex) {

		}
	}
}
