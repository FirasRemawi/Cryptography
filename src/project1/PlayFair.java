package project1;

public class PlayFair {
	private char[][] table;
	private String keyword;

	public PlayFair(String keyword) {
		this.keyword = keyword.toUpperCase().replaceAll("[^A-Z]", "");
		this.table = generateTable(this.keyword);
	}

	public char[][] generateTable(String keyword) {
		char[][] table = new char[5][5];
		boolean[] added = new boolean[26];

		// Fill the table with the keyword
		int index = 0;
		for (char c : keyword.toCharArray()) {
			if (!added[c - 'A'] && c != 'J') {
				table[index / 5][index % 5] = c;
				added[c - 'A'] = true;
				index++;
			}
		}

		for (char c = 'A'; c <= 'Z'; c++) {
			if (!added[c - 'A'] && c != 'J') {
				table[index / 5][index % 5] = c;
				added[c - 'A'] = true;
				index++;
			}
		}

		return table;
	}

	private String repairText(String text, boolean forEncryption) {
		text = text.toUpperCase().replaceAll("[^A-Z]", "");
		StringBuilder preparedText = new StringBuilder();
		char prev = '\0';

		for (char c : text.toCharArray()) {
			if (c == 'J')
				c = 'I'; // Replace 'J' with 'I'
			if (forEncryption && c == prev) {
				preparedText.append('X'); // Insert 'X' between duplicate letters
			}
			preparedText.append(c);
			prev = c;
		}

		if (forEncryption && preparedText.length() % 2 != 0) {
			preparedText.append('X'); // Add 'X' if the length is odd
		}
		if (!forEncryption && preparedText.length() % 2 != 0) {
			preparedText.setLength(preparedText.length() - 1);
			// remove 'X' if the length is odd
		}

		return preparedText.toString();
	}

	private int[] findPosition(char c, char[][] table) {
		for (int row = 0; row < table.length; row++) {
			for (int col = 0; col < table[row].length; col++) {
				if (table[row][col] == c) {
					return new int[] { row, col };
				}
			}
		}
		return null;
	}

	public String encrypt(String text, char[][] table) {
		text = repairText(text, true);
		StringBuilder encryptedText = new StringBuilder();

		for (int i = 0; i < text.length(); i += 2) {
			int[] pos1 = findPosition(text.charAt(i), table);
			int[] pos2 = findPosition(text.charAt(i + 1), table);

			if (pos1[0] == pos2[0]) { // Same row
				encryptedText.append(table[pos1[0]][(pos1[1] + 1) % 5]);
				encryptedText.append(table[pos2[0]][(pos2[1] + 1) % 5]);
			} else if (pos1[1] == pos2[1]) { // Same column
				encryptedText.append(table[(pos1[0] + 1) % 5][pos1[1]]);
				encryptedText.append(table[(pos2[0] + 1) % 5][pos2[1]]);
			} else { // Rectangle
				encryptedText.append(table[pos1[0]][pos2[1]]);
				encryptedText.append(table[pos2[0]][pos1[1]]);
			}
		}

		return encryptedText.toString();
	}

	public String decrypt(String text, char[][] table) {
		StringBuilder decryptedText = new StringBuilder();
		text = text.toUpperCase().replaceAll("[^A-Z]", "");

		for (int i = 0; i < text.length(); i += 2) {
			int[] pos1 = findPosition(text.charAt(i), table);
			int[] pos2 = findPosition(text.charAt(i + 1), table);

			if (pos1 != null && pos2 != null) {
				if (pos1[0] == pos2[0]) { // Same row
					decryptedText.append(table[pos1[0]][(pos1[1] + 4) % 5]);
					decryptedText.append(table[pos2[0]][(pos2[1] + 4) % 5]);
				} else if (pos1[1] == pos2[1]) { // Same column
					decryptedText.append(table[(pos1[0] + 4) % 5][pos1[1]]);
					decryptedText.append(table[(pos2[0] + 4) % 5][pos2[1]]);
				} else { // Rectangle
					decryptedText.append(table[pos1[0]][pos2[1]]);
					decryptedText.append(table[pos2[0]][pos1[1]]);
				}
			} else {
				// Handle the case where the character was not found in the table
				throw new IllegalArgumentException("Character not found in PlayFair table during decryption.");
			}
		}

		// Remove any padding characters that may have been added during encryption
		String decrypted = decryptedText.toString();
		if (decrypted.endsWith("X")) {
			decrypted = decrypted.substring(0, decrypted.length() - 1);
		}

		return decrypted;
	}

	public void displayTable() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				System.out.print(table[i][j] + " ");
			}
			System.out.println(); // Move to the next line after printing each row
		}
	}

	public char[][] getTable() {
		return table;
	}

	public void setTable(char[][] table) {
		this.table = table;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public static void main(String[] args) {
		/*
		 * PlayFair cipher = new PlayFair("hello"); String encrypted =
		 * cipher.encrypt("Firas"); System.out.println("Encrypted: " + encrypted);
		 * System.out.println(cipher.decrypt(encrypted)); cipher.displayTable();
		 */

	}
}
