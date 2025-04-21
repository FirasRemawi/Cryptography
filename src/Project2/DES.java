package Project2;

public class DES {

	private final String[] hexToBin = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001",
			"1010", "1011", "1100", "1101", "1110", "1111" };

	private final char[] binToHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14,
			6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45,
			37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

	private final int[] E = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17,
			18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

	private final int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9,
			19, 13, 30, 6, 22, 11, 4, 25 };

	private final int[] FP = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
			30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42,
			10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

	private final int[][][] SBOXES = {
			/* SBox1 */
			{ { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
					{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
					{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
					{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
			/* SBox2 */
			{ { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
					{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
					{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
					{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
			/* SBox3 */
			{ { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
					{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
					{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
					{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
			/* SBox4 */
			{ { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
					{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
					{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
					{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
			/* SBox5 */
			{ { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
					{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
					{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
					{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
			/* SBox6 */
			{ { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
					{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
					{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
					{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
			/* SBox7 */
			{ { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
					{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
					{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
					{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
			/* SBox8 */
			{ { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
					{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
					{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
					{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };

	private final int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11,
			3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13,
			5, 28, 20, 12, 4 };

	private final int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
			41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

	private final int[] shift = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

	private String initialPermutation, finalPermutation, left, right, expandedRight, xored, sBoxed, permuted, newRight;

	String stringToHex(String str) {
		StringBuilder hex = new StringBuilder();
		for (char ch : str.toCharArray()) {
			hex.append(String.format("%02X", (int) ch));
		}
		return hex.toString();
	}

	String hexToString(String hex) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		return str.toString();
	}

	String binToHex(String bin) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bin.length(); i += 4) {
			String binSegment = bin.substring(i, i + 4);
			int decimal = Integer.parseInt(binSegment, 2);
			hex.append(Integer.toHexString(decimal).toUpperCase());
		}
		return hex.toString();
	}

	public String hexToBin(String s) {
		StringBuilder bin = new StringBuilder();
		for (char c : s.toCharArray()) {
			int digit = Character.digit(c, 16);
			if (digit == -1) {
				throw new IllegalArgumentException("Invalid hexadecimal character: " + c);
			}
			bin.append(hexToBin[digit]);
		}
		return bin.toString();
	}

	// XOR two binary strings
	public String xor(String a, String b) {
		StringBuilder ans = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			ans.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
		}
		return ans.toString();
	}

	// Permute function to rearrange the bits
	public String permute(String str, int[] arr, int n) {
		StringBuilder permutation = new StringBuilder();
		for (int i = 0; i < n; i++) {
			permutation.append(str.charAt(arr[i] - 1));
		}
		return permutation.toString();
	}

	// shifting the bits towards left by nth shifts
	public String shiftLeft(String str, int nthShifts) {
		String s = str;
		for (int i = 0; i < nthShifts; i++) {
			s = s.substring(1) + s.charAt(0);
		}
		return s;
	}

	public String encryptBlock(String blockHex, String[] roundKeys) {
		initialPermutation = permute(hexToBin(blockHex), IP, 64);
		left = initialPermutation.substring(0, 32);
		right = initialPermutation.substring(32, 64);

		for (int j = 0; j < 16; j++) {
			expandedRight = permute(right, E, 48);
			xored = xor(expandedRight, roundKeys[j]);
			sBoxed = sBoxFunction(xored);
			permuted = permute(sBoxed, P, 32);
			newRight = xor(left, permuted);
			left = right;
			right = newRight;
		}

		finalPermutation = permute(right + left, FP, 64); // Final swap is included
		return binToHex(finalPermutation);
	}

	public String decryptBlock(String blockHex, String[] roundKeys) {
		initialPermutation = permute(hexToBin(blockHex), IP, 64);
		left = initialPermutation.substring(0, 32);
		right = initialPermutation.substring(32, 64);

		for (int j = 0; j < 16; j++) {
			expandedRight = permute(right, E, 48);
			xored = xor(expandedRight, roundKeys[j]);
			sBoxed = sBoxFunction(xored);
			permuted = permute(sBoxed, P, 32);
			newRight = xor(left, permuted);
			left = right;
			right = newRight;
		}

		finalPermutation = permute(right + left, FP, 64); // Final swap is included
		return binToHex(finalPermutation);
	}

	public String encrypt(String hexPlaintext, String[] roundKeys) {
		StringBuilder cipherTextBuilder = new StringBuilder();
		for (int i = 0; i < hexPlaintext.length(); i += 16) {
			String block = hexPlaintext.substring(i, Math.min(i + 16, hexPlaintext.length()));
			cipherTextBuilder.append(encryptBlock(block, roundKeys));
		}
		return cipherTextBuilder.toString();
	}

	public String decrypt(String hexCiphertext, String[] roundKeys) {
		StringBuilder plainTextBuilder = new StringBuilder();
		for (int i = 0; i < hexCiphertext.length(); i += 16) {
			String block = hexCiphertext.substring(i, Math.min(i + 16, hexCiphertext.length()));
			plainTextBuilder.append(decryptBlock(block, roundKeys));
		}
		return plainTextBuilder.toString();
	}

	private String sBoxFunction(String input) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < 8; i++) {// retreving row and column values by getting the decimal value
			int row = 2 * Character.getNumericValue(input.charAt(i * 6))
					+ Character.getNumericValue(input.charAt(i * 6 + 5));
			int col = 8 * Character.getNumericValue(input.charAt(i * 6 + 1))
					+ 4 * Character.getNumericValue(input.charAt(i * 6 + 2))
					+ 2 * Character.getNumericValue(input.charAt(i * 6 + 3))
					+ Character.getNumericValue(input.charAt(i * 6 + 4));
			int value = SBOXES[i][row][col];
			output.append(String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0'));
		}
		return output.toString();
	}

	public String[] generateRoundKeys(String key) {
		String[] roundKeys = new String[16];
		String left = key.substring(0, 28);
		String right = key.substring(28, 56);

		for (int i = 0; i < 16; i++) {
			left = shiftLeft(left, shift[i]);
			right = shiftLeft(right, shift[i]);
			String combinedKey = left + right;
			roundKeys[i] = permute(combinedKey, PC2, 48);
		}

		return roundKeys;
	}

	// Method to pad the plaintext to ensure it is a multiple of 64 bits (8 bytes)
	String padPlaintext(String plaintext) {
		int paddingLength = 8 - (plaintext.length() % 8);
		StringBuilder paddedText = new StringBuilder(plaintext);
		char paddingChar = (char) paddingLength;
		for (int i = 0; i < paddingLength; i++) {
			paddedText.append(paddingChar);
		}
		return paddedText.toString();
	}

	// Method to remove padding after decryption
	String removePadding(String plaintext) {
		int paddingLength = plaintext.charAt(plaintext.length() - 1);
		return plaintext.substring(0, plaintext.length() - paddingLength);
	}

	public static void main(String[] args) {
		DES des = new DES();
		String pt = "firas";
		System.out.println(des.stringToHex(pt));
		String key = "22234512987ABB23";

		// Convert key to binary and permute using PC1
		String binKey = des.hexToBin(key);
		String permutedKey = des.permute(binKey, des.getPc1(), 56); // Using PC1 for key permutation

		// Generate round keys
		String[] roundKeys = des.generateRoundKeys(permutedKey);

		// Pad plaintext
		String paddedPlaintext = des.padPlaintext(pt);
		System.out.println(paddedPlaintext);
		// Convert padded plaintext to hex
		String hexPlaintext = des.stringToHex(paddedPlaintext);
		System.out.println(hexPlaintext);
		// Encrypting the plaintext
		String cipherText = des.encrypt(hexPlaintext, roundKeys);
		System.out.println("Cipher Text: " + des.hexToString(cipherText));
		System.out.println("Cipher Text Length: " + cipherText.length());

		// Decryption needs the round keys in reverse order
		String[] reversedRoundKeys = new String[roundKeys.length];
		for (int i = 0; i < roundKeys.length; i++) {
			reversedRoundKeys[i] = roundKeys[roundKeys.length - 1 - i];
		}

		// Decrypting the ciphertext
		String decryptedHex = des.decrypt(cipherText, reversedRoundKeys);

		// Convert decrypted hex back to padded plaintext
		String paddedDecryptedText = des.hexToString(decryptedHex);

		// Remove padding from decrypted text
		String decryptedText = des.removePadding(paddedDecryptedText);
		System.out.println("Decrypted Text: " + decryptedText);
	}

	public String[] getHextobinary() {
		return hexToBin;
	}

	public char[] getBinarytohex() {
		return binToHex;
	}

	public int[] getIp() {
		return IP;
	}

	public int[] getE() {
		return E;
	}

	public int[] getP() {
		return P;
	}

	public int[] getFp() {
		return FP;
	}

	public int[][][] getSboxes() {
		return SBOXES;
	}

	public int[] getPc1() {
		return PC1;
	}

	public int[] getPc2() {
		return PC2;
	}

	public int[] getShifts() {
		return shift;
	}

	public String getInitialPermutation() {
		return initialPermutation;
	}

	public String getFinalPermutation() {
		return finalPermutation;
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public String getExpandedRight() {
		return expandedRight;
	}

	public String getXored() {
		return xored;
	}

	public String getsBoxed() {
		return sBoxed;
	}

	public String getPermuted() {
		return permuted;
	}

	public String getNewRight() {
		return newRight;
	}
}
