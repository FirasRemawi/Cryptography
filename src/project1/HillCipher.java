package project1;

public class HillCipher {
	private int[][] keyMatrix;
	private int[][] inverseKeyMatrix;

	public HillCipher(int[][] keyMatrix) {
		this.keyMatrix = keyMatrix;
		this.inverseKeyMatrix = calculateInverseMatrix(keyMatrix);
	}

	public HillCipher() {

	}

	public int[][] getKeyMatrix() {
		return keyMatrix;
	}

	public void setKeyMatrix(int[][] keyMatrix) {
		this.keyMatrix = keyMatrix;
		this.inverseKeyMatrix = calculateInverseMatrix(keyMatrix);
	}

	public String encrypt(String input, int[][] keyMatrix) {
		input = input.toUpperCase().replaceAll("[^A-Z]", ""); // Standardize input
		StringBuilder encryptedText = new StringBuilder();
		while (input.length() % 3 != 0) {
			input += "X"; // Padding
		}

		for (int i = 0; i < input.length(); i += 3) {
			int[] block = { input.charAt(i) - 'A', input.charAt(i + 1) - 'A', input.charAt(i + 2) - 'A' };
			int[] encryptedBlock = multiplyMatrix(block, keyMatrix);
			for (int j : encryptedBlock) {
				encryptedText.append((char) ('A' + j));
			}
		}
		return encryptedText.toString();
	}

	public String decrypt(String output, int[][] keyMatrix) {
		if (output.length() % 3 != 0)
			return "";
		int[][] inverseKeyMatrix = calculateInverseMatrix(keyMatrix); // Calculate inverse key matrix
		output = output.toUpperCase().replaceAll("[^A-Z]", ""); // Standardize output
		StringBuilder decryptedText = new StringBuilder();

		for (int i = 0; i < output.length(); i += 3) {
			int[] block = { output.charAt(i) - 'A', output.charAt(i + 1) - 'A', output.charAt(i + 2) - 'A' };
			int[] decryptedBlock = multiplyMatrix(block, inverseKeyMatrix);
			for (int j : decryptedBlock) {
				decryptedText.append((char) ('A' + j));
			}
		}

		// Remove padding if necessary (OPTIONAL)
		while (decryptedText.length() > 0 && decryptedText.charAt(decryptedText.length() - 1) == 'X') {
			decryptedText.setLength(decryptedText.length() - 1);
		}

		return decryptedText.toString();
	}

	private int[][] calculateInverseMatrix(int[][] matrix) {
		// Calculation of the determinant of a 3x3 matrix
		int det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2])
				- matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
				+ matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);

		int detInv = mod26Inverse(det, 26);
		if (detInv == -1) {
			throw new IllegalArgumentException("Matrix is not invertible.");
		}

		// Apply formula to calculate the inverse of a 3x3 matrix modulo 26
		int[][] inverse = new int[3][3];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				// Calculate cofactor of element [row,col]
				int sign = ((row + col) % 2 == 0) ? 1 : -1;
				int[][] subMatrix = new int[2][2];
				for (int i = 0, r = 0; i < 3; i++) {
					if (i == row)
						continue;
					for (int j = 0, c = 0; j < 3; j++) {
						if (j == col)
							continue;
						subMatrix[r][c++] = matrix[i][j];
					}
					r++;
				}
				int subDet = subMatrix[0][0] * subMatrix[1][1] - subMatrix[0][1] * subMatrix[1][0];
				inverse[col][row] = mod26(detInv * sign * subDet); // Transpose and multiply by detInv
			}
		}

		return inverse;
	}

	private int mod26Inverse(int a, int m) {
		a = (a % m + m) % m; // Normalize 'a' to be positive
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1) {
				return x;
			}
		}
		return -1; // Indicate failure to find an inverse
	}

	private int mod26(int value) {
		return (value % 26 + 26) % 26;
	}

	private int[] multiplyMatrix(int[] block, int[][] matrix) {
		int[] result = new int[block.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < block.length; j++) {
				result[i] += block[j] * matrix[i][j];
			}
			result[i] = mod26(result[i]);
		}
		return result;
	}

	public static void main(String[] args) {
		KeyGenerator keyGen = new KeyGenerator(15);
		int[][] keyMatrix = { { 14, 17, 5 }, { 1, 9, 0 }, { 9, 7, 13 } };
		HillCipher hill = new HillCipher();
		if (hill.getKeyMatrix() == null)
			hill.setKeyMatrix(keyMatrix);
		String plaintext = "NQYOTOH";
		String encrypted = hill.encrypt(plaintext, hill.getKeyMatrix());
		String decrypted = hill.decrypt(encrypted, hill.getKeyMatrix());

		System.out.println("Encrypted: " + encrypted);
		System.out.println("Decrypted: " + decrypted);
	}

}
