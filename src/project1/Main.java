package project1;

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

public class Main extends Application {
    Map<Character, Integer> map = new HashMap<Character, Integer>();
    String s = "aasjjikkk";
int i = 0;
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
    private VBox vbHillEncryption, vbHillDecryption, vbPlayFairEncryption, vbPlayFairDecryption, vbOTPEncryption,
            vbOTPDecryption;
    private TextField tfPassword, tfInput, tfSeed, tfKey, tfEmail;
    private PasswordField pfPassword;
    private Button btnEnter, toggleButton, btnFile, btnOk;
    ;
    private String correctPassword, enteredPassword;
    private Hyperlink link;
    private HillCipher hill = new HillCipher();
    private PlayFair playFair;
    private OneTimePad otp;
    private GridPane gp;
    private CheckBox chEmail, chFile;
    private KeyGenerator keyGenerator;
    private String cipherText;
    private Alert alert;
    private int[][] matrix;
    private String plainText;
    private StringBuilder lineFile;
for(
        private StringBuilder cipherTxt; i<s.length();i++)

    {
        char c = s.charAt(i);
        Integer val = map.get(c);
        if (val != null) {
            map.put(c, val + 1);
        } else {
            map.put(c, 1);
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
        hillDecrypt.setOnAction(e -> setupHillDecryptionUI(primaryStage));
        menuHill.getItems().addAll(hillEncrypt, hillDecrypt);

        menuPlayfair = new Menu("PlayFair cipher");
        playFairEncrypt = new MenuItem("Encrypt");
        playFairEncrypt.setOnAction(e -> setupPlayFairEncryptionUI(primaryStage));
        playFairDecrypt = new MenuItem("Decrypt");
        playFairDecrypt.setOnAction(e -> setupPlayFairDecryptionUI(primaryStage));
        menuPlayfair.getItems().addAll(playFairEncrypt, playFairDecrypt);

        menuOTP = new Menu("One Time Pad cipher");
        otpEncrypt = new MenuItem("Encrypt");
        otpEncrypt.setOnAction(e -> setupOTPEncryptionUI(primaryStage));
        otpDecrypt = new MenuItem("Decrypt");
        otpDecrypt.setOnAction(e -> setupOTPDecryptionUI(primaryStage));
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
        vbHillEncryption = new VBox();
        vbHillDecryption = new VBox();
        vbOTPDecryption = new VBox();
        vbOTPEncryption = new VBox();
        vbPlayFairDecryption = new VBox();
        vbPlayFairEncryption = new VBox();
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

    private void setupPlayFairDecryptionUI(Stage primaryStage) {
        scene.setRoot(bp);
        btnOk.setText("Decrypt");
        clearGridPane();
        setupGridPaneForDecryption();
        bp.setCenter(gp);
        ta.setEditable(false);
        clearInputFields();
        bp.setBottom(ta);
        extracted();
        btnOk.setOnAction(e -> handleDecryption("playFair"));
        tfSeed.setOnAction(e -> updateOTPKeyDisplay());
        setupFileChooser();
    }

    private void setupOTPDecryptionUI(Stage primaryStage) {
        scene.setRoot(bp);
        btnOk.setText("Decrypt");
        clearGridPane();
        setupGridPaneForDecryption();
        bp.setCenter(gp);
        ta.setEditable(false);
        clearInputFields();
        bp.setBottom(ta);
        extracted();
        btnOk.setOnAction(e -> handleDecryption("OTP"));
        tfSeed.setOnAction(e -> updateOTPKeyDisplay());
        setupFileChooser();
    }

    private void setupPlayFairEncryptionUI(Stage primaryStage) {
        scene.setRoot(bp);
        btnOk.setText("Encrypt");
        clearGridPane();
        setupGridPaneForEncryption();
        bp.setCenter(gp);
        ta.setEditable(false);
        clearInputFields();
        bp.setBottom(ta);
        extracted();
        btnOk.setOnAction(e -> handleEncryption("playFair"));
        tfSeed.setOnAction(e -> updateOTPKeyDisplay());
        setupFileChooser();
    }

    private void setupOTPEncryptionUI(Stage primaryStage) {
        scene.setRoot(bp);
        btnOk.setText("Encrypt");
        clearGridPane();
        setupGridPaneForEncryption();
        bp.setCenter(gp);
        ta.setEditable(false);
        clearInputFields();
        bp.setBottom(ta);
        extracted();
        btnOk.setOnAction(e -> handleEncryption("OTP"));
        tfSeed.setOnAction(e -> updateOTPKeyDisplay());
        setupFileChooser();
    }

    private void setupHillDecryptionUI(Stage primaryStage) {
        scene.setRoot(bp);
        btnOk.setText("Decrypt");
        clearGridPane();
        setupGridPaneForDecryption();
        bp.setCenter(gp);
        ta.setEditable(false);
        clearInputFields();
        bp.setBottom(ta);
        extracted();
        btnOk.setOnAction(e -> handleDecryption("Hill"));
        tfSeed.setOnAction(e -> updateHillKeyDisplay());
        setupFileChooser();
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

    private void handleDecryption(String algorithm) {
        if (validateInputs()) {
            switch (algorithm) {
                case "Hill":
                    performHillDecryption();
                    break;
                case "OTP":
                    performOTPDecryption();
                    break;
                case "playFair":
                    performPlayFairDecryption();

            }
            updateUIPostDecryption();
        } else {
            showAlert("Please fill all necessary fields and ensure they are valid!", Alert.AlertType.ERROR);
        }
    }

    private void performOTPDecryption() {
        try {
            // Check if the file checkbox is selected and read from the file if necessary
            String base64Ciphertext;
            if (chFile.isSelected()) {
                base64Ciphertext = readFromFile();
            } else {
                base64Ciphertext = tfInput.getText();
            }

            // Ensure the base64Ciphertext is properly padded
            base64Ciphertext = base64Ciphertext.trim(); // Trim the whitespace
            while (base64Ciphertext.length() % 4 != 0) {
                base64Ciphertext = base64Ciphertext + "="; // Pad with equals signs
            }

            // Initialize the OneTimePad instance if necessary
            if (otp == null) {
                otp = new OneTimePad();
            }

            // Initialize the key; if no last used key is available, create a new one
            byte[] key = (otp.getLastUsedKey() != null) ? otp.getLastUsedKey()
                    : new KeyGenerator(Long.parseLong(tfSeed.getText().trim()))
                    .generateByteArrayKey(base64Ciphertext.length());

            // Decrypt the message
            cipherText = otp.decrypt(base64Ciphertext, key);

            // Display the result
            ta.setText("Ciphertext: " + base64Ciphertext + "\nDecrypted to -> " + cipherText);
            writeToFile(cipherText);

        } catch (Exception e) {
            showAlert("Decryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void performHillDecryption() {
        try {
            if (chFile.isSelected()) {
                readFromFile();
            }
            if (hill == null) {
                hill = new HillCipher();
            }

            // Initialize the key; if no last used key is available, create a new one
            int[][] matrix = (hill.getKeyMatrix() != null) ? hill.getKeyMatrix()
                    : new KeyGenerator(Long.parseLong(tfSeed.getText().trim())).generateHillKeyMatrix(3);
            String sourceText = chFile.isSelected() ? String.valueOf(lineFile) : tfInput.getText();
            cipherText = hill.decrypt(sourceText, matrix);
            ta.setText(sourceText + " is Decrypted to -> " + cipherText);
        } catch (Exception e) {
            showAlert("Decryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void performPlayFairDecryption() {
        try {
            String cipherText = chFile.isSelected() ? String.valueOf(lineFile) : tfInput.getText();
            String keyword;
            // Assume you have a TextField tfKey for the keyword
            if (playFair == null) {
                keyword = keyGenerator.generatePlayfairKeyword(tfSeed.getLength());
                playFair = new PlayFair(keyword);

            }
            String decryptedText = playFair.decrypt(cipherText, playFair.getTable());

            ta.setText(cipherText + " is Decrypted to -> " + decryptedText);
            writeToFile(decryptedText); // If needed
        } catch (Exception e) {
            showAlert("Decryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
        btnOk.setOnAction(e -> handleEncryption("Hill"));
        tfSeed.setOnAction(e -> updateHillKeyDisplay());
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

    private void handleEncryption(String algorithm) {
        if (validateInputs()) {
            switch (algorithm) {
                case "Hill":
                    performHillEncryption();
                    break;
                case "OTP":
                    performOTPEncryption();
                    break;
                case "playFair":
                    performPlayFairEncryption();
                    break;
            }
            updateUIPostEncryption();
        } else {
            showAlert("Please fill all necessary fields and ensure they are valid!", Alert.AlertType.ERROR);
        }
    }
    /*
     * In this method, we generate an OTP key using user's seed, and encrypting the
     * input we got (File or input text)
     */

    private boolean validateInputs() {
        return !tfSeed.getText().isEmpty() && isNumeric(tfSeed.getText().trim())
                && (chFile.isSelected() || !tfInput.getText().isEmpty());
    }
    /*
     * In this method, we generate a playFair key using user's seed, and encrypting
     * the input we got (File or input text)
     */

    /*
     * In this method, we generate a hill key using user's seed, and encrypting the
     * input we got (File or input text)
     */
    private void performHillEncryption() {
        try {
            keyGenerator = new KeyGenerator(Long.parseLong(tfSeed.getText().trim()));
            matrix = keyGenerator.generateHillKeyMatrix(3);
            hill = new HillCipher();
            hill.setKeyMatrix(matrix);
            String sourceText = chFile.isSelected() ? String.valueOf(lineFile) : tfInput.getText();
            cipherText = hill.encrypt(sourceText, matrix);
            ta.setText(sourceText + " is Encrypted to -> " + cipherText);
            writeToFile(cipherText);
        } catch (Exception e) {
            showAlert("Encryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void performOTPEncryption() {
        try {
            otp = new OneTimePad();
            String sourceText = chFile.isSelected() ? String.valueOf(lineFile) : tfInput.getText();
            keyGenerator = new KeyGenerator(Long.parseLong(tfSeed.getText().trim()));
            byte[] key = keyGenerator.generateByteArrayKey(sourceText.length());
            otp.setKey(key);
            cipherText = otp.encrypt(sourceText, key);
            ta.setText(sourceText + " is Encrypted to -> " + cipherText);
            writeToFile(cipherText);
        } catch (Exception e) {
            showAlert("Encryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void performPlayFairEncryption() {
        try {
            keyGenerator = new KeyGenerator(Long.parseLong(tfSeed.getText()));
            String sourceText = chFile.isSelected() ? String.valueOf(lineFile) : tfInput.getText();
            String keyword = keyGenerator.generatePlayfairKeyword(tfSeed.getLength());

            // The PlayFair constructor will handle table generation
            playFair = new PlayFair("Hello");

            cipherText = playFair.encrypt(sourceText, playFair.getTable());
            ta.setText(playFair.getKeyword());

            ta.appendText(sourceText + " is Encrypted to -> " + cipherText);
            writeToFile(cipherText);
        } catch (Exception e) {
            showAlert("Encryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

    private void updateHillKeyDisplay() {
        if (!tfSeed.getText().isEmpty() && isNumeric(tfSeed.getText().trim())) {
            int[][] matrix = null;
            if (hill != null)// check if user used decryption first
                matrix = hill.getKeyMatrix();
            if (matrix != null) {
                StringBuilder keyString = new StringBuilder();
                for (int[] row : matrix) {
                    for (int element : row) {
                        keyString.append(element).append(" ");
                    }
                }
                tfKey.setText(keyString.toString());
                tfKey.setDisable(false);
            } else {
                showAlert("Key matrix is null.", Alert.AlertType.ERROR);
                tfKey.setDisable(true);
            }
        } else {
            showAlert("Seed must be a numeric value and cannot be empty.", Alert.AlertType.ERROR);
            tfKey.setDisable(true);
        }
    }

    private void updateOTPKeyDisplay() {
        if (!tfSeed.getText().isEmpty() && isNumeric(tfSeed.getText().trim())) {
            byte[] matrix = null;
            if (otp != null)// check if user used decryption first
                matrix = otp.getLastUsedKey();
            if (matrix != null) {
                StringBuilder keyString = new StringBuilder();
                for (byte row : matrix) {
                    keyString.append(row);
                }
                tfKey.setText(keyString.toString());
                tfKey.setDisable(false);
            } else {
                showAlert("Key matrix is null.", Alert.AlertType.ERROR);
                tfKey.setDisable(true);
            }
        } else {
            showAlert("Seed must be a numeric value and cannot be empty.", Alert.AlertType.ERROR);
            tfKey.setDisable(true);
        }
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
}
