package application;

import java.security.SecureRandom;

public class RandomKeyGenerator {
	public static void main(String[] args) {
		// Initialize a SecureRandom instance
		SecureRandom secureRandom = new SecureRandom();

		// Generate a random key (byte array)
		byte[] randomKey = new byte[16]; // Adjust the size as needed
		secureRandom.nextBytes(randomKey);

		// Convert the byte array to a hexadecimal string (for display)
		StringBuilder hexKey = new StringBuilder();
		for (byte b : randomKey) {
			hexKey.append(String.format("%02X", b));
		}

		System.out.println("Random Key (Hex): " + hexKey.toString());
	}
}
