package application;

public class VernamCipher {

	private static String toBinaryString(String input) {
		StringBuilder binary = new StringBuilder();
		byte[] bytes = input.getBytes();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}

	private static String fromBinaryString(String binary) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < binary.length(); i += 8) {
			int charCode = Integer.parseInt(binary.substring(i, i + 8), 2);
			text.append((char) charCode);
		}
		return text.toString();
	}

	private static String xorBinaryStrings(String binary1, String binary2) {
		StringBuilder xor = new StringBuilder();
		for (int i = 0; i < binary1.length(); i++) {
			xor.append(binary1.charAt(i) == binary2.charAt(i) ? "0" : "1");
		}
		return xor.toString();
	}

	public static void main(String[] args) {
		String originalText = "oak";
		String key = "son";

		String binaryText = toBinaryString(originalText);
		String binaryKey = toBinaryString(key);

		String encryptedBinary = xorBinaryStrings(binaryText, binaryKey);

		String decryptedBinary = xorBinaryStrings(encryptedBinary, binaryKey);
		String decryptedText = fromBinaryString(decryptedBinary);

		System.out.println("Original Binary: " + binaryText);
		System.out.println("Encrypted Binary: " + encryptedBinary);
		System.out.println("Decrypted Text: " + decryptedText);
	}
}
