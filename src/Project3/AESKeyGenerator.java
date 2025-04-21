package Project3;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Formatter;

public class AESKeyGenerator {
	private static final int KEY_SIZE = 16; // 128 bits = 16 bytes
	private static final int EXPANDED_KEY_SIZE = 176; // 11 * 16 bytes
	private static final byte[] Rcon = { (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20,
			(byte) 0x40, (byte) 0x80, (byte) 0x1B, (byte) 0x36 };

	public AESKeyGenerator() {
		super();
	}
	// Dr.Abdullah's method
//	public static byte[] generateAESKey(int size) {
//		try {
//			// Generate AES key
//			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//			int keySize = 128; // Change this to 128, 192, or 256
//			keyGenerator.init(keySize);
//			SecretKey key = keyGenerator.generateKey();
//
//			// Get the encoded format of the key (byte array)
//			byte[] encodedKey = key.getEncoded();
//			return encodedKey;
//		} catch (NoSuchAlgorithmException e) {
//			return null;
//		}
//	}

	public byte[] generateAESKey(String seed, int size) throws AESException {
		if (size != 128 && size != 192 && size != 256)
			throw new AESException("Size should be 128/192/256");

		try {
			// Create a SecureRandom instance seeded with the provided seed
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed.getBytes());

			// Generate AES key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(size, secureRandom); // 128-bit AES key

			SecretKey key = keyGenerator.generateKey();

			// Get the encoded format of the key (byte array)
			byte[] encodedKey = key.getEncoded();
			return encodedKey;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static int getKeySize() {
		return KEY_SIZE;
	}

	public static int getExpandedKeySize() {
		return EXPANDED_KEY_SIZE;
	}

	public static byte[] getRcon() {
		return Rcon;
	}

	public static void main(String[] args) throws AESException {
		AESKeyGenerator g = new AESKeyGenerator();
		String seed = "firas";
		byte[] aesKey = g.generateAESKey(seed, 192);
		byte[] aesKey1 = g.generateAESKey(seed, 192);
		System.out.println("Generated AES Key with Seed:");
		for (byte b : aesKey) {
			System.out.printf("%02x ", b);
		}
		System.out.println();
		for (byte b : aesKey1) {
			System.out.printf("%02x ", b);
		}
	}

}
