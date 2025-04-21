package application;

public class HillCipher {
	private  int[][] keyMatrix;
	private int[][] inverseKeyMatrix;

	public HillCipher(int[][] keyMatrix) {
		this.keyMatrix = keyMatrix;
		this.inverseKeyMatrix = calculateInverseMatrix(keyMatrix);
	}

	private int[][] calculateInverseMatrix(int[][] matrix) {
		int a = matrix[0][0], b = matrix[0][1], c = matrix[1][0], d = matrix[1][1];
		int det = mod26Inverse(a * d - b * c, 26);
		if (det == -1) {
			throw new IllegalArgumentException("Matrix is not invertible.");
		}
		// Applying formula for inverse of 2x2 matrix modulo 26.
		return new int[][] { { mod26(det * d), mod26(-det * b) }, { mod26(-det * c), mod26(det * a) } };
	}

	private int mod26Inverse(int a, int m) {
		a = (a % m + m) % m; // Normalize 'a' to be positive
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1) {
				return x;
			}
		}
		throw new IllegalArgumentException("No modular inverse found for " + a + " in mod " + m);
	}

	private int mod26(int value) {
		return (value % 26 + 26) % 26;
	}

	private int[] multiplyMatrix(int[] block, int[][] matrix) {
		int[] result = new int[2];
		result[0] = mod26(block[0] * matrix[0][0] + block[1] * matrix[0][1]);
		result[1] = mod26(block[0] * matrix[1][0] + block[1] * matrix[1][1]);
		return result;
	}

	// Implementation of Algorithms interface methods
	public String encrypt(String input, int key) {
		input = input.toUpperCase();
		StringBuilder encryptedText = new StringBuilder();
		// Ensure input length is even for 2x2 matrix processing.
		if (input.length() % 2 != 0) {
			input += "X"; // Append 'X' to make the input length even.
		}

		for (int i = 0; i < input.length(); i += 2) {
			int[] block = { input.charAt(i) - 'A', input.charAt(i + 1) - 'A' };
			int[] encryptedBlock = multiplyMatrix(block, keyMatrix);
			encryptedText.append((char) ('A' + encryptedBlock[0])).append((char) ('A' + encryptedBlock[1]));
		}
		return encryptedText.toString();
	}

	public String decrypt(String output, int key) {
		output = output.toUpperCase();
		StringBuilder decryptedText = new StringBuilder();
		for (int i = 0; i < output.length(); i += 2) {
			int[] block = { output.charAt(i) - 'A', output.charAt(i + 1) - 'A' };
			int[] decryptedBlock = multiplyMatrix(block, inverseKeyMatrix);
			decryptedText.append((char) ('A' + decryptedBlock[0])).append((char) ('A' + decryptedBlock[1]));
		}
		return decryptedText.toString();
	}
	

	public int[][] getKeyMatrix() {
		return keyMatrix;
	}
	public void setKeyMatrix(int[][] inverseKeyMatrix) {
		this.inverseKeyMatrix = inverseKeyMatrix;
	}
	public void setInverseKeyMatrix(int[][] inverseKeyMatrix) {
		this.inverseKeyMatrix = inverseKeyMatrix;
	}

	public static void main(String[] args) {
		int[][] keyMatrix = { { 3, 10 }, { 15, 17 } }; // This matrix is invertible modulo 26
		HillCipher cipher = new HillCipher(keyMatrix);
		String plaintext = "firas"; // Note: For simplicity, plaintext length should be even.
		String encrypted = cipher.encrypt(plaintext, 0); // Key parameter is unused, so passing 0
		String decrypted = cipher.decrypt(encrypted, 0); // Key parameter is unused, so passing 0

		System.out.println("Plaintext: " + plaintext);
		System.out.println("Encrypted: " + encrypted);
		System.out.println("Decrypted: " + decrypted);
	}

}
