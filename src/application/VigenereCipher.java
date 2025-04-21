package application;

public class VigenereCipher {

	// Encrypts text using a key
	public static String encryptText(String text, String key, boolean autoKey) {
		String res = "";
		text = text.toUpperCase();
		key = generateKey(text, key, autoKey).toUpperCase();
		for (int i = 0; i < text.length(); i++) {
			int x = (text.charAt(i) - 'A' + key.charAt(i) - 'A') % 26;
			x += 'A';
			res += (char) (x);
		}
		return res;
	}

	// Decrypts encrypted text
	public static String decryptText(String cipherText, String key, boolean autoKey) {
		String res = "";
		cipherText = cipherText.toUpperCase();
		key = generateKey(cipherText, key, autoKey).toUpperCase();
		for (int i = 0; i < cipherText.length(); i++) {
			int x = (cipherText.charAt(i) - key.charAt(i) + 26) % 26;
			x += 'A';
			res += (char) (x);
		}
		return res;
	}

	// Generates key, adjusts for auto key if necessary
	public static String generateKey(String text, String key, boolean autoKey) {
		if (autoKey) {
			// Adjust for the case where the key might be longer than the text
			if (key.length() >= text.length()) {
				return key.substring(0, text.length());
			}
			return key + text.substring(0, text.length() - key.length());
		} else {
			int x = text.length() / key.length();
			int y = text.length() % key.length();
			return key.repeat(x) + key.substring(0, y);
		}
	}

	// Generates a random key of a given length
	public static String generateRandomKey(int length) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = (int) (Math.random() * alphabet.length());
			res.append(alphabet.charAt(index));
		}
		return res.toString();
	}

	public static void main(String[] args) {
		VigenereCipher cipher = new VigenereCipher();
		String plaintext = "firas"; // Note: For simplicity, plaintext length should be even.
		String encrypted = VigenereCipher.encryptText("wearediscoveredsaveyourself", "deceptive", true); // Key
																											// parameter
																											// is
																											// unused,
																											// so
																											// passing 0
		// String decrypted = cipher.decrypt(encrypted, 0); // Key parameter is unused,
		// so passing 0

		System.out.println("Plaintext: " + "wearediscoveredsaveyourself");
		System.out.println("Encrypted: " + encrypted);
		// System.out.println("Decrypted: " + decrypted);
	}
}
