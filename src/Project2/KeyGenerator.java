package Project2;

import java.util.Random;

public class KeyGenerator {
	private final Random random;
	private String seed;

	public KeyGenerator(String seed) {
		this.seed = seed;
		this.random = new Random(seed.hashCode());// Using a reproducible byte array from seed string for consistent
													// Random
		// initialization
	}

	public String generateDESKeyHex() {
		byte[] key = new byte[8]; // 8 bytes for 64 bits
		random.nextBytes(key);
		StringBuilder hexString = new StringBuilder();
		for (byte b : key) {
			String hex = Integer.toHexString(0xFF & b); // Convert the byte to hex
			if (hex.length() == 1) {
				hexString.append('0'); // Pad with '0' if only one digit
			}
			hexString.append(hex);
		}
		return hexString.toString().toUpperCase(); // Return the hex string in upper case
	}

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	public String generateDESKeyHex1() {
		byte[] key = new byte[8]; // 8 bytes for 64 bits
		random.nextBytes(key);
		StringBuilder hexString = new StringBuilder();
		for (byte b : key) {
			String hex = Integer.toHexString(0xFF & b); // Convert the byte to hex

			hexString.append(hex);
		}
		return hexString.toString().toUpperCase(); // Return the hex string in upper case
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
		// Reset Random with new seed
		this.random.setSeed((long) seed.hashCode());
	}

	public byte[] generateByteArrayKey(int length) {
		byte[] key = new byte[length];
		random.nextBytes(key);
		return key;
	}
}
