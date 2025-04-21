package project1;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
	private final SecureRandom secureRandom;
	private long seed;

	public KeyGenerator(long seed) {
		this.secureRandom = new SecureRandom();
		this.secureRandom.setSeed(seed); // Initialize SecureRandom with a seed for reproducibility
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public byte[] generateByteArrayKey(int length) {
		byte[] key = new byte[length];
		secureRandom.nextBytes(key);
		return key;
	}

	// Generates a keyword for Playfair Cipher from a random alphanumeric string
	public String generatePlayfairKeyword(int length) {
		StringBuilder keyword = new StringBuilder();
		while (keyword.length() < length) {
			char c = (char) (secureRandom.nextInt(26) + 'A');
			if (keyword.indexOf(String.valueOf(c)) == -1 && c != 'J') {
				keyword.append(c);
			}
		}
		return keyword.toString();
	}

	public int[][] generateHillKeyMatrix(int size) {
		if (size != 3) {
			throw new IllegalArgumentException("This method currently supports only 3x3 matrices.");
		}
		int[][] matrix;
		int det;
		do {
			matrix = new int[size][size];
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					matrix[i][j] = secureRandom.nextInt(26);
				}
			}
			det = calculateDeterminant(matrix);
		} while (gcd(det, 26) != 1);
		return matrix;
	}

	private int calculateDeterminant(int[][] matrix) {
		int a = matrix[0][0], b = matrix[0][1], c = matrix[0][2];
		int d = matrix[1][0], e = matrix[1][1], f = matrix[1][2];
		int g = matrix[2][0], h = matrix[2][1], i = matrix[2][2];

		return a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);
	}

	private int gcd(int a, int b) {
		while (b != 0) {
			int t = b;
			b = a % b;
			a = t;
		}
		return a;
	}

	public static void main(String[] args) {
		KeyGenerator keyGenerator = new KeyGenerator(System.currentTimeMillis()); // Use current time as seed

		// Example usage for a 128-bit key for OTP

		// Example usage for Playfair Cipher
		String playfairKey = keyGenerator.generatePlayfairKeyword(8);
		System.out.println("Playfair Keyword: " + playfairKey);

		// Example usage for Hill Cipher (2x2 matrix)
		int[][] hillKeyMatrix = keyGenerator.generateHillKeyMatrix(3);
		System.out.println("Hill Cipher Key Matrix: ");
		for (int[] row : hillKeyMatrix) {
			for (int element : row) {
				System.out.print(element + " ");
			}
			System.out.println();
		}
	}
}
