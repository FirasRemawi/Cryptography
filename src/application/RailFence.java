package application;

public class RailFence {

	public RailFence() {

	}

	public static String encryptRailFence(String text, int key) {
		char[][] rail = new char[key][text.length()];
		for (int i = 0; i < key; i++) {
			for (int j = 0; j < text.length(); j++) {
				rail[i][j] = '\n';
			}
		}
		boolean down = false;
		int row = 0, col = 0;
		for (int i = 0; i < text.length(); i++) {
			if (row == 0 || row == key - 1) {
				down = !down;
			}
			rail[row][col++] = text.charAt(i);
			row = down ? row + 1 : row - 1;
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < key; i++) {
			for (int j = 0; j < text.length(); j++) {
				if (rail[i][j] != '\n') {
					result.append(rail[i][j]);
				}
			}
		}
		return result.toString().toUpperCase();
	}

	public static String decryptRailFence(String cipher, int key) {
		char[][] rail = new char[key][cipher.length()];
		for (int i = 0; i < key; i++)
			for (int j = 0; j < cipher.length(); j++)
				rail[i][j] = '\n';

		boolean dirDown = false;
		int row = 0, col = 0;
		for (int i = 0; i < cipher.length(); i++) {
			if (row == 0)
				dirDown = true;
			if (row == key - 1)
				dirDown = false;

			rail[row][col++] = '*';

			row = dirDown ? row + 1 : row - 1;
		}
		int index = 0;
		for (int i = 0; i < key; i++)
			for (int j = 0; j < cipher.length(); j++)
				if (rail[i][j] == '*' && index < cipher.length())
					rail[i][j] = cipher.charAt(index++);

		StringBuilder result = new StringBuilder();
		row = 0;
		col = 0;
		dirDown = true; // Reset direction to down at start
		for (int i = 0; i < cipher.length(); i++) {
			result.append(rail[row][col++]);

			if (row == 0)
				dirDown = true;
			else if (row == key - 1)
				dirDown = false;
			row = dirDown ? row + 1 : row - 1;
		}
		return result.toString().toUpperCase();
	}

	public static void main(String[] args) {
		String plainText = "FirasRemawi";
		int key = 2;
		String encryptedText = encryptRailFence(plainText.toUpperCase(), key);
		System.out.println("Encrypted: " + encryptedText);
		String decryptedText = decryptRailFence(encryptedText, key);
		System.out.println("Decrypted: " + decryptedText);
	}
}
