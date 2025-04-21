package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

public class Cryptography extends Application {
	private MenuBar menuBar;
	private Menu menu1, menu2, menu3, menu4;
	private MenuItem item1, item21, item22, item11, item3, item4, item44, item444, item5, item55;
	private BorderPane bp, bpOperations, bpHill;
	private Label lblInput, lblKey;
	private TextField tfInput, tfKey, tfKey2, tfKey3, tfKey4;
	private VBox vb, vb1, vbCaesar, vbHill, vbVigenere;
	private HBox hbInput, hbKey, hbOptions, hbButtons, hbKeyVigenere, hbInputVigenere, hbKey1, hbOptionsVigenere,
			hbButtonsVigenere;
	private Button btContinue, btOk, btClear;
	private Image image, image2, image3, image4;
	private ImageView imageView, imageView2, imageView1, imageViewMainPage, imageView3;
	private Text txtWelcoming;
	private TextArea ta;
	private ComboBox<String> cmBox;
	private RadioButton rb1, rb2, rb3;
	private ToggleGroup tg;
	private Scene scene, sceneOperations, sceneHelp, sceneHill, sceneDisplay;
	private CaesarCipher caesar;
	private Monoalphabetic mono;
	private HBox hbKey3;
	private TextField tfKey1;
	private HillCipher hill;
	private HBox hbKey2;
	private File f;
	private FileChooser fChooser;
	private Scanner read;

	@Override
	public void start(Stage primaryStage) {
		menuBar = new MenuBar();
		menuBar.resize(80, 80);
		menu1 = new Menu("File");
		imageView1 = new ImageView(new Image("file.png"));
		imageView1.setFitWidth(50);
		imageView1.setFitHeight(50);
		menu1.setGraphic(imageView1);
		image = new Image("https://freepngimg.com/thumb/folder/20-folder-png-image.png");
		imageView = new ImageView(image);
		imageView.setFitWidth(20);
		imageView.setFitHeight(20);
		item1 = new MenuItem("Open");
		item1.setGraphic(imageView);

		menu1.getItems().add(item1);

		menu2 = new Menu("Help center");
		item21 = new MenuItem("About us");
		item22 = new MenuItem("Firas's system");
		image2 = new Image("firas.png");
		imageView2 = new ImageView(image2);
		imageView2.setFitWidth(50);
		imageView2.setFitHeight(50);
		menu2.setGraphic(imageView2);
		menu2.getItems().addAll(item21, item22);

		menu3 = new Menu("");
		menu3.setText("Operations");
		image3 = new Image("crypto.png");
		image4 = new Image("OIP.jpeg");
		imageView3 = new ImageView(image3);
		imageView3.setFitWidth(50);
		imageView3.setFitHeight(50);
		menu3.setGraphic(imageView3);
		item3 = new MenuItem("proceed");
		menu3.getItems().add(item3);

		menu4 = new Menu("Algorithms");
		menu4.setVisible(false);
		cmBox = new ComboBox<>();
		cmBox.getItems().addAll("Caesar cipher", "Monoalphabetic cipher", "Hill cipher", "Vigenere cipher");
		menu4.setGraphic(cmBox);
		item3.setOnAction(e -> {
			operationsPage(primaryStage);
		});

		menuBar.getMenus().addAll(menu1, menu3, menu4, menu2);

		bpOperations = new BorderPane();

		fChooser = new FileChooser();
		fChooser.setInitialDirectory(new File("C:\\Users\\ASUS\\eclipse-workspace\\Cryptography"));

		txtWelcoming = new Text("Welocme !" + "Here is the main page, Enjoy exploring the provided algorithms 😁");
		txtWelcoming.setStyle("-fx-alignment: center;\r\n" + "-fx-font-size: 20;-fx-font-weight: bold;");
		imageViewMainPage = new ImageView(new Image("Firas-company.png"));
		ta = new TextArea();
		ta.setEditable(false);
		ta.setPrefHeight(350);
		ta.setPrefWidth(650);
		ta.setMaxHeight(400);
		btContinue = new Button("Try Now");
		// btContinue.setGraphic(new ImageView(new Image ("try.jpeg")));
		btContinue.setAlignment(Pos.CENTER);
		btContinue.setPadding(new Insets(15, 15, 15, 15));
		vb1 = new VBox(15);
		vb1.setAlignment(Pos.CENTER);
		vb1.getChildren().addAll(ta, btContinue);
		// btContinue.setMaxSize(20, 20);
		vb = new VBox(10);
		vb.getChildren().addAll(imageViewMainPage, txtWelcoming);
		vb.setAlignment(Pos.CENTER);

		vbCaesar = new VBox(15);
		lblInput = new Label("Input ");
		tfInput = new TextField();
		tfInput.setPromptText("e.g. Firas");
		hbInput = new HBox(15);
		hbInput.getChildren().addAll(lblInput, tfInput);
		lblKey = new Label("Key");
		tfKey = new TextField();
		tfKey.setPromptText("e.g. 3");

		hbKey = new HBox(15);
		hbKey.getChildren().addAll(lblKey, tfKey);

		hbInput.setAlignment(Pos.CENTER);
		hbKey.setAlignment(Pos.CENTER);
		vbCaesar.setAlignment(Pos.CENTER);

		rb1 = new RadioButton("Encryption");
		rb2 = new RadioButton("Decryption");
		rb3 = new RadioButton("Brute force");
		tg = new ToggleGroup();
		rb1.setToggleGroup(tg);
		rb2.setToggleGroup(tg);
		rb3.setToggleGroup(tg);

		hbOptions = new HBox(15);
		hbOptions.getChildren().addAll(rb1, rb2, rb3);
		hbOptions.setAlignment(Pos.CENTER);

		btOk = new Button("Ok");
		btClear = new Button("Clear");
		hbButtons = new HBox(15);
		hbButtons.getChildren().addAll(btOk, btClear);
		hbButtons.setAlignment(Pos.CENTER);
		vbCaesar.getChildren().addAll(hbOptions, hbInput, hbKey, hbButtons);

		vbHill = new VBox(15);
		tfKey1 = new TextField();
		tfKey1.setPromptText("K11");
		tfKey2 = new TextField();
		tfKey2.setPromptText("K12");
		tfKey3 = new TextField();
		tfKey3.setPromptText("K21");
		tfKey4 = new TextField();
		tfKey4.setPromptText("K22");
		hbKey2 = new HBox(15);
		hbKey2.getChildren().addAll(new Label("Keys"), tfKey1, tfKey2);
		hbKey2.setAlignment(Pos.CENTER);
		hbKey3 = new HBox(15);
		hbKey3.getChildren().addAll(new Label("    "), tfKey3, tfKey4);
		hbKey3.setAlignment(Pos.CENTER);
		vbHill.setAlignment(Pos.CENTER);

		bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(vb);
		//
		menuBar.setPadding(new Insets(10, 10, 10, 10));
		scene = new Scene(bp, 800, 800);
		sceneOperations = new Scene(bpOperations, 800, 800);
		bpHill = new BorderPane();
		sceneHill = new Scene(bpHill, 800, 800);
		// sceneHelp= new Scene(null);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Firas's Cryptography Application"); // Set the title of the window
		item1.setOnAction(e -> {
			f = fChooser.showOpenDialog(primaryStage);

		});
		primaryStage.show();

	}

	private void MainPage(Stage primaryStage) {
		primaryStage.setScene(scene);
		menu4.setVisible(false);
		menu3.setText("Operations");
		imageView3.setImage(image3);
		menu3.setGraphic(imageView3);
		bp.setTop(menuBar);
		bp.setCenter(vb);
		item3.setOnAction(e -> {
			operationsPage(primaryStage);
		});
		primaryStage.show();
	}

	private void operationsPage(Stage primaryStage) {
		primaryStage.setScene(sceneOperations);
		menu3.setText("Main Menu");
		imageView3.setImage(image4);
		menu3.setGraphic(imageView3);
		menu4.setVisible(true);
		bpOperations.setTop(menuBar);
		bpOperations.setCenter(vb1);
		ta.setVisible(true);
		ta.clear();
		BorderPane.setAlignment(btContinue, Pos.CENTER);
		VBox opVbox = new VBox(25);
		opVbox.setAlignment(Pos.TOP_CENTER); // This line ensures the VBox contents are centered
		opVbox.getChildren().addAll(cmBox);

		cmBox.setOnAction(e -> {
			if (cmBox.getValue() == "Caesar cipher") {
				ta.clear();

				ta.setStyle("-fx-alignment: center;\r\n" + "-fx-font-size: 25;-fx-font-weight: bold;");
				ta.setText("Caesar Cipher\r\n"
						+ "The earliest known, and the simplest, use of a substitution cipher was by Julius \r\n"
						+ "Caesar. The Caesar cipher involves replacing each letter of the alphabet with the \r\n"
						+ "letter standing three places further down the alphabet.\n For example,\r\n"
						+ "plain: meet me after the toga party\r\n" + "cipher: PHHW PH DIWHU WKH WRJD SDUWB");
				ta.appendText("\nEncryption formula: C = E(k, p) = (p + k) mod 26");
				ta.appendText("\nDecryption formula: p = D(k, C) = (C - k) mod 26");
				btContinue.setOnAction(e1 -> {
					caesarPage(primaryStage);

				});
			} else if (cmBox.getValue() == "Monoalphabetic cipher") {
				ta.clear();

				ta.setStyle("-fx-alignment: center;\r\n" + "-fx-font-size: 25;-fx-font-weight: bold;");
				ta.setText("Monoalphabetic Ciphers\r\n"
						+ "With only 25 possible keys, the Caesar cipher is far from secure. A dramatic increase \r\n"
						+ "in the key space can be achieved by allowing an arbitrary substitution. Before proceeding, we define the term permutation. A permutation of a finite set of elements S\r\n"
						+ "is an ordered sequence of all the elements of S, with each element appearing exactly \r\n"
						+ "once. For example, if S = {a, b, c}, there are six permutations of S:\r\n"
						+ " abc, acb, bac, bca, cab, cba \r\n"
						+ "In general, there are n! permutations of a set of n elements, because the first \r\n"
						+ "element can be chosen in one of n ways, the second in n - 1 ways, the third in n - 2 \r\n"
						+ "ways, and so on.\r\n" + "Recall the assignment for the Caesar cipher:\r\n"
						+ "plain: a b c d e f g h i j k l m n o p q r s t u v w x y z\r\n"
						+ "cipher: D E F G H I J K L M N O P Q R S T U V W X Y Z A B C");

				btContinue.setOnAction(e1 -> {
					monoAlphabeticPage(primaryStage);
				});
			} else if (cmBox.getValue() == "Hill cipher") {
				ta.clear();

				ta.setStyle("-fx-alignment: center;\r\n" + "-fx-font-size: 25;-fx-font-weight: bold;");
				ta.setText("Hill Cipher\r\n"
						+ "Another interesting multiletter cipher is the Hill cipher, developed by the mathematician Lester Hill in 1929.\r\n"
						+ "CONCEPTS FROM LINEAR ALGEBRA Before describing the Hill cipher, let us briefly \r\n"
						+ "review some terminology from linear algebra. In this discussion, we are concerned \r\n"
						+ "with matrix arithmetic modulo 26. For the reader who needs a refresher on matrix \r\n"
						+ "multiplication and inversion, see Appendix E.\r\n" + "We define the inverse M^-1\r\n"
						+ " of a square matrix M by the equation M(M^-1\r\n" + ") =\r\n" + "M^-1\r\n"
						+ "M = I, where I is the identity matrix. I is a square matrix that is all zeros except \r\n"
						+ "for ones along the main diagonal from upper left to lower right. The inverse of a \r\n"
						+ "matrix does not always exist, but when it does, it satisfies the preceding equation.");
				btContinue.setOnAction(e1 -> {
					HillPage(primaryStage);
				});
			} else if (cmBox.getValue() == "Vigenere cipher") {
				ta.clear();

				ta.setStyle("-fx-alignment: center;\r\n" + "-fx-font-size: 15;-fx-font-weight: bold;");
				ta.setText("VIGENÈRE CIPHER The best known, and one of the simplest, polyalphabetic ciphers \r\n"
						+ "is the Vigenère cipher. In this scheme, the set of related monoalphabetic substitution rules consists of the 26 Caesar ciphers with shifts of 0 through 25. Each cipher is \r\n"
						+ "denoted by a key letter, which is the ciphertext letter that substitutes for the plaintext letter a. Thus, a Caesar cipher with a shift of 3 is denoted by the key value 3.8\r\n"
						+ "We can express the Vigenère cipher in the following manner. Assume a \r\n"
						+ "sequence of plaintext letters P = p0, p1, p2, c , pn-1 and a key consisting of the \r\n"
						+ "sequence of letters K = k0, k1, k2, c , km-1, where typically m 6 n. The sequence \r\n"
						+ "of ciphertext letters C = C0, C1, C2, c , Cn-1 is calculated as follows:\r\n"
						+ "C = C0, C1, C2, c , Cn-1 = E(K, P) = E[(k0, k1, k2, c , km-1), (p0, p1, p2, c , pn-1)]\r\n"
						+ "= (p0 + k0) mod 26, (p1 + k1) mod 26, c ,(pm-1 + km-1) mod 26,\r\n"
						+ "(pm + k0) mod 26, (pm+1 + k1) mod 26, c , (p2m-1 + km-1) mod 26, c\r\n"
						+ "Thus, the first letter of the key is added to the first letter of the plaintext, mod 26, \r\n"
						+ "the second letters are added, and so on through the first m letters of the plaintext. \r\n"
						+ "For the next m letters of the plaintext, the key letters are repeated. This process \r\n"
						+ "8\r\n"
						+ "To aid in understanding this scheme and also to aid in it use, a matrix known as the Vigenère tableau is \r\n"
						+ "often used. This tableau is discussed in a document at box.com/Crypto7e.\r\n"
						+ "3.2 / SUBSTITUTION TECHNIQUES 103\r\n"
						+ "continues until all of the plaintext sequence is encrypted. A general equation of the \r\n"
						+ "encryption process is\r\n" + " Ci = (pi + ki mod m) mod 26 (3.3)\r\n"
						+ "Compare this with Equation (3.1) for the Caesar cipher. In essence, each plaintext character is encrypted with a different Caesar cipher, depending on the corresponding key character. Similarly, decryption is a generalization of Equation (3.2):\r\n"
						+ " pi = (Ci - ki mod m) mod 26 (3.4)\r\n"
						+ "To encrypt a message, a key is needed that is as long as the message. Usually, \r\n"
						+ "the key is a repeating keyword. For example, if the keyword is deceptive, the message “we are discovered save yourself” is encrypted as\r\n"
						+ "key: deceptivedeceptivedeceptive\r\n" + "plaintext: wearediscoveredsaveyourself\r\n"
						+ "ciphertext: ZICVTWQNGRZGVTWAVZHCQYGLMGJ");
				btContinue.setOnAction(e1 -> {
					VigenerePage(primaryStage);
				});
			}

		});
		item3.setOnAction(e -> {
			MainPage(primaryStage);
		});
		primaryStage.show();
	}

	private void VigenerePage(Stage primaryStage) {
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
		Button btnFile = new Button("Get From File");
		Button btnDecrypt = new Button("Decrypt");
		Button btnRandomKey = new Button("Generate Random Key");
		TextArea txtResult = new TextArea();
		txtResult.setEditable(false);

		grid.add(new Label("Plaintext/Ciphertext:"), 0, 0);
		grid.add(txtPlaintext, 1, 0);
		grid.add(new Label("Key:"), 0, 1);
		grid.add(txtKey, 1, 1);
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
		BorderPane bpVigenere = new BorderPane();
		bpVigenere.setCenter(grid);
		bpVigenere.setTop(menuBar);
		item3.setOnAction(e -> {
			MainPage(primaryStage);
		});
		btnFile.setOnAction(e -> {
			if (f == null) {
				f = fChooser.showOpenDialog(primaryStage);
			} else {
				try {
					read = new Scanner(f);
				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				}
				while (read.hasNext()) {
					StringBuilder st = new StringBuilder();
					st.append(read.nextLine());
					String randomKey = VigenereCipher.generateRandomKey(10); // Example length of 10
					txtKey.setText(randomKey);

				}
			}
		});
		Scene scene = new Scene(bpVigenere, 800, 800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void monoAlphabeticPage(Stage primaryStage) {
		ta.clear();
		MainPage(primaryStage);
		menu3.setText("Main Menu");
		imageView3.setImage(image4);
		menu3.setGraphic(imageView3);
		menu4.setVisible(false);
		bp.setCenter(vbCaesar);
		bp.setBottom(ta);

		rb3.setVisible(false);

		rb1.setOnAction(e -> {
			ta.clear();
			tfInput.clear();
			tfKey.clear();
			btOk.setOnAction(e1 -> {
				mono = new Monoalphabetic();
				try {
					String output = mono.encrypt(tfInput.getText(), 0);
					ta.setText(tfInput.getText() + " is Encrypted to -> " + output);
				} catch (NumberFormatException ex) {
					// Display an error message or log the error.
					ta.setText("Error: Please enter a valid number for the key.");
				}
			});
		});
		rb2.setOnAction(e -> {
			mono = new Monoalphabetic();
			ta.clear();
			btOk.setOnAction(e1 -> {
				if (tfInput.getText().isEmpty()) {
					ta.setText("Please enter both text and a key for decryption.");
					return;
				}

				try {
					String output = mono.decrypt(tfInput.getText(), 0); // Directly using tfInput to fetch the text
					ta.setText(tfInput.getText() + " is Decrypted to -> " + output);
				} catch (NumberFormatException ex) {
					ta.setText("Error: Please enter a valid number for the key.");
				}
			});
		});
		item3.setOnAction(e -> {

			operationsPage(primaryStage);
		});
		primaryStage.show();

	}

	private void caesarPage(Stage primaryStage) {
		ta.clear();
		MainPage(primaryStage);
		menu3.setText("Main Menu");
		imageView3.setImage(image4);
		menu3.setGraphic(imageView3);
		menu4.setVisible(false);
		bp.setCenter(vbCaesar);
		bp.setBottom(ta);
		item3.setOnAction(e -> {
			MainPage(primaryStage);
		});
		caesar = new CaesarCipher();

		rb3.setVisible(true);
		rb1.setOnAction(e -> {
			ta.clear();
			tfInput.clear();
			tfKey.clear();
			btOk.setOnAction(e1 -> {
				try {
					String output = caesar.encrypt(tfInput.getText(), 3);
					ta.setText(tfInput.getText() + " is Encrypted to -> " + output);
				} catch (NumberFormatException ex) {
					// Display an error message or log the error.
					ta.setText("Error: Please enter a valid number for the key.");
				}
			});
		});
		rb2.setOnAction(e -> {
			ta.clear();
			btOk.setOnAction(e1 -> {
				if (tfInput.getText().isEmpty() || tfKey.getText().isEmpty()) {
					ta.setText("Please enter both text and a key for decryption.");
					return;
				}

				try {
					int key = Integer.parseInt(tfKey.getText()); // Directly using tfKey to fetch the text
					String output = caesar.decrypt(tfInput.getText(), key); // Directly using tfInput to fetch the text
					ta.setText(tfInput.getText() + " with key " + key + " is Decrypted to -> " + output);
				} catch (NumberFormatException ex) {
					ta.setText("Error: Please enter a valid number for the key.");
				}
			});
		});

		// Set action for rb3 (Brute force)
		rb3.setOnAction(e -> {
			ta.clear();
			btOk.setOnAction(e1 -> {
				// Perform brute force decryption with all possible keys
				if (tfInput.getText().isEmpty()) {
					ta.setText("Please enter both text and a key for decryption.");
					return;
				}
				try {
					for (int i = 0; i < 26; i++) {
						String output = caesar.decrypt(tfInput.getText(), i); // Directly using tfInput to fetch the
																				// text
						ta.appendText(tfInput.getText() + " with key " + i + " is Decrypted to -> " + output + "\n");
					}
				} catch (NumberFormatException ex) {
					ta.setText("Error: Please enter a valid number for the key.");
				}
			});
		});

		btClear.setOnAction(e -> {
			tfInput.clear();
			tfKey.clear();
			ta.clear();
		});
		item3.setOnAction(e -> {
			operationsPage(primaryStage);
		});
		primaryStage.show();
	}

	private void HillPage(Stage primaryStage) {
		primaryStage.setScene(sceneHill);
		menu3.setText("Main Menu");
		imageView3.setImage(image4);
		menu3.setGraphic(imageView3);
		menu4.setVisible(false);
		bpHill.setCenter(vbCaesar);
		bpHill.setBottom(ta);
		ta.clear();
		bpHill.setTop(menuBar);
		item3.setOnAction(e -> {
			MainPage(primaryStage);
		});

		rb3.setVisible(false);
		rb1.setOnAction(e -> {
			ta.clear();
			try {
				// Generate random keys and check for invertibility
				int k11, k12, k21, k22, det;
				int[][] keyMatrix;
				do {
					k11 = (int) (Math.random() * 26);
					k12 = (int) (Math.random() * 26);
					k21 = (int) (Math.random() * 26);
					k22 = (int) (Math.random() * 26);
					det = k11 * k22 - k12 * k21;
					keyMatrix = new int[][] { { k11, k12 }, { k21, k22 } };
				} while (gcd(det, 26) != 1);
				hill = new HillCipher(keyMatrix);
				btOk.setOnAction(e1 -> {

					// Assuming that hill.encrypt() does not require a key parameter as the key is
					// part of the state
					String encrypted = hill.encrypt(tfInput.getText(), 0);
					ta.setText(tfInput.getText() + " is encrypted to -> " + encrypted);
				});

			} catch (Exception ex) {
				ta.setText("Error during encryption: " + ex.getMessage());
			}
		});
		rb2.setOnAction(e -> {
			ta.clear();
			tfInput.clear();
			try {
				// Assuming you have a valid decryption key matrix already set up
				// Perhaps it is saved from a previous encryption or provided by the user
				// The HillCipher object would need to be initialized with this valid key matrix
				// beforehand
				btOk.setOnAction(e1 -> {

					// Assuming that hill.encrypt() does not require a key parameter as the key is
					// part of the state
					String decrypted = hill.decrypt(tfInput.getText(), 0);
					ta.setText(tfInput.getText() + " is decrypted to -> " + decrypted);
				});

			} catch (Exception ex) {
				ta.setText("Error during decryption: " + ex.getMessage());
			}
		});

		// Set action for rb3 (Brute force)

		btClear.setOnAction(e -> {
			tfInput.clear();
			tfKey.clear();
			ta.clear();
		});
		item3.setOnAction(e -> {
			operationsPage(primaryStage);
		});
		primaryStage.show();
	}

	public static int gcd(int a, int b) {
		if (b == 0) {
			return a;
		}
		return gcd(b, a % b);
	}

	public static void main(String[] args) {
		launch(args);
	}/*
		 * private void setupDistrictScreen() { // Assuming this is called once during
		 * initialization progressBar = new ProgressBar(0);
		 * progressBar.setVisible(false); // Initially not visible
		 * vbDistrictInsert.getChildren().addAll(progressBar); // Add it to your layout
		 * as appropriate
		 * 
		 * // Set the action for btOkDis here if it's not dynamically changing
		 * btOkDis.setOnAction(e -> insertDistrictOperation()); }
		 * 
		 * private void insertDistrictOperation() { progressBar.setVisible(true);
		 * progressBar.setProgress(0); // Reset progress to 0
		 * 
		 * Thread taskThread = new Thread(() -> { for (int i = 1; i <= 10; i++) { try {
		 * Thread.sleep(100); // Simulate task by sleeping for 100 ms } catch
		 * (InterruptedException interruptedException) {
		 * interruptedException.printStackTrace(); } final double progress = i * 0.1;
		 * javafx.application.Platform.runLater(() ->
		 * progressBar.setProgress(progress)); }
		 * 
		 * javafx.application.Platform.runLater(() -> progressBar.setVisible(false)); //
		 * Hide progress bar after task }); taskThread.start(); // Start the task
		 * simulation }
		 * 
		 * private void showDistrictScreen(Stage primaryStage) { // This assumes
		 * setupDistrictScreen() is called during your application initialization
		 * bp.setLeft(vbDistrict); vbDistrict.setVisible(true); rb11.setOnAction(e -> {
		 * if (rb11.isSelected()) { vbDistrictInsert.getChildren().clear();
		 * vbDistrictInsert.getChildren().addAll(hbDistrictInsert,
		 * hbDistrictInsertButtons, progressBar);
		 * vbDistrictInsert.setAlignment(Pos.CENTER); bp.setCenter(vbDistrictInsert); }
		 * else { bp.setCenter(null); // Remove from center if not selected } }); }
		 */
}