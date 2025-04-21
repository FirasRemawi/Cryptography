package Project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class AES extends AESComponents {

	private static int NUMBEROFROUNDS;
	private static int kEYSIZE;
	private static int EXPANDEDKEYSIZE;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	// Attributes for each operation
	private byte[] initialState;
	private byte[] byteSubState;
	private byte[] shiftRowState;
	private byte[] mixColumnsState;
	private byte[] roundKeyAddedState;
	private byte[] finalState;

	// Attributes for the key schedule process
	private byte[] rotWordState;
	private byte[] subWordState;
	private byte rconState;

	public static byte[] DiffusionLayer(byte[] input) throws AESException {
		input = shiftRow(input);
		input = mixColumns(input);
		return input;
	}

	private static byte galoisMultiply(byte a, byte b) {
		byte c = 0;
		byte highestBit;
		for (int counter = 0; counter < 8; counter++) {
			if ((b & 1) != 0) {
				c ^= a;
			}
			highestBit = (byte) (a & 0x80);
			a <<= 1;
			if (highestBit != 0) {
				a ^= 0x1b;
			}
			b >>= 1;
		}
		return c;
	}

	public static byte[] mixColumns(byte[] input) throws AESException {
		if (input == null || input.length != 16) {
			throw new AESException("Input must be a non-null array of length 16");
		}

		byte[][] stateArray = new byte[4][4];

		for (int i = 0; i < 16; i++) {
			stateArray[i % 4][i / 4] = input[i];
		}

		for (int col = 0; col < 4; col++) {
			byte[] column = new byte[4];
			for (int row = 0; row < 4; row++) {
				column[row] = stateArray[row][col];
			}
			mixColumn(column);
			for (int row = 0; row < 4; row++) {
				stateArray[row][col] = column[row];
			}
		}

		byte[] output = new byte[16];
		for (int i = 0; i < 16; i++) {
			output[i] = stateArray[i % 4][i / 4];
		}

		return output;
	}

	private static void mixColumn(byte[] column) {
		byte[] temp = new byte[4];
		temp[0] = (byte) (galoisMultiply((byte) 0x02, column[0]) ^ galoisMultiply((byte) 0x03, column[1]) ^ column[2]
				^ column[3]);
		temp[1] = (byte) (column[0] ^ galoisMultiply((byte) 0x02, column[1]) ^ galoisMultiply((byte) 0x03, column[2])
				^ column[3]);
		temp[2] = (byte) (column[0] ^ column[1] ^ galoisMultiply((byte) 0x02, column[2])
				^ galoisMultiply((byte) 0x03, column[3]));
		temp[3] = (byte) (galoisMultiply((byte) 0x03, column[0]) ^ column[1] ^ column[2]
				^ galoisMultiply((byte) 0x02, column[3]));

		System.arraycopy(temp, 0, column, 0, 4);
	}

	// Left shift rows based on index
	public static byte[] shiftRow(byte[] input) throws AESException {
		if (input == null || input.length != 16) {
			throw new AESException("Input must be a non-null array of length 16");
		}

		byte[][] state = new byte[4][4];

		for (int i = 0; i < 16; i++) {
			state[i % 4][i / 4] = input[i];
		}
		for (int i = 1; i < 4; i++) {
			state[i] = leftShift(state[i], i);
		}

		byte[] output = new byte[16];
		for (int i = 0; i < 16; i++) {
			output[i] = state[i % 4][i / 4];
		}
		return output;
	}

	private static byte[] leftShift(byte[] row, int shift) {
		byte[] newRow = new byte[4];
		for (int i = 0; i < 4; i++) {
			newRow[i] = row[(i + shift) % 4];
		}
		return newRow;
	}

	// We will extract the ROW/COLUMN Indecies and subtitute them from the SBOX
	public static byte[] byteSub(byte[] input) throws AESException {
		if (input == null) {
			throw new AESException("Input cannot be null");
		}
		byte[] output = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			int byteValue = input[i] & 0xFF;
			int row = (byteValue >> 4) & 0x0F;
			int col = byteValue & 0x0F;
			int sBoxValue = getSbox()[(row << 4) | col];
			output[i] = (byte) sBoxValue;
		}
		return output;
	}

	public static byte[] addRoundKey(byte[] state, byte[] roundKey) throws AESException {
		if (state == null || roundKey == null) {
			throw new AESException("State and roundKey must be non-null arrays of length 16");
		}

		byte[] newState = new byte[16];
		for (int i = 0; i < 16; i++) {
			newState[i] = (byte) (state[i] ^ roundKey[i]);
		}
		return newState;
	}

	public byte[] expandKey(byte[] key) throws AESException {
		kEYSIZE = key.length;
		if (kEYSIZE == 16) {
			EXPANDEDKEYSIZE = 176;
			NUMBEROFROUNDS = 10;
		} else if (kEYSIZE == 24) {
			EXPANDEDKEYSIZE = 208;
			NUMBEROFROUNDS = 12;
		} else if (kEYSIZE == 32) {
			EXPANDEDKEYSIZE = 240;
			NUMBEROFROUNDS = 14;
		} else {
			throw new AESException("Invalid key size. Key size must be 128, 192, or 256 bits.");
		}
		byte[] expandedKey = new byte[EXPANDEDKEYSIZE];
		System.arraycopy(key, 0, expandedKey, 0, kEYSIZE);
		byte[] temp = new byte[4];
		int bytesGenerated = kEYSIZE;
		int rconIndex = 0;
		while (bytesGenerated < EXPANDEDKEYSIZE) {
			System.arraycopy(expandedKey, bytesGenerated - 4, temp, 0, 4);
			if (bytesGenerated % kEYSIZE == 0) {
				temp = SubWord(RotWord(temp));
				rotWordState = temp.clone();
				temp[0] ^= getRcon()[rconIndex++];
				rconState = getRcon()[rconIndex - 1];
			} else if (kEYSIZE > 24 && bytesGenerated % kEYSIZE == 16) {
				temp = SubWord(temp);
			}
			subWordState = temp.clone();
			for (int i = 0; i < 4; i++) {
				expandedKey[bytesGenerated] = (byte) (expandedKey[bytesGenerated - kEYSIZE] ^ temp[i]);
				bytesGenerated++;
			}
		}
		return expandedKey;
	}

	private byte[] RotWord(byte[] word) {
		byte temp = word[0];
		for (int i = 0; i < 3; i++) {
			word[i] = word[i + 1];
		}
		word[3] = temp;
		return word;
	}

	private byte[] SubWord(byte[] word) {
		for (int i = 0; i < word.length; i++) {
			word[i] = (byte) getSbox()[word[i] & 0xFF];
		}
		return word;
	}

	public String padPlaintext(String plaintext) {
		int blockSize = 16;
		int paddingLength = blockSize - (plaintext.length() % blockSize);
		char paddingChar = (char) paddingLength;
		StringBuilder paddedText = new StringBuilder(plaintext);
		for (int i = 0; i < paddingLength; i++) {
			paddedText.append(paddingChar);
		}
		return paddedText.toString();
	}

	public String removePadding(String plaintext) {
		int paddingLength = (int) plaintext.charAt(plaintext.length() - 1);
		return plaintext.substring(0, plaintext.length() - paddingLength);
	}

	/*
	 * Encrypt--> We will encrypt plain text based on the mode. if ECB each 128 bit
	 * block is seperate.
	 */
	public String encrypt(String plaintext, byte[] key, String mode) throws AESException {
		byte[] roundKeys = expandKey(key);
		StringBuilder cipherTextBuilder = new StringBuilder();

		// Pad the plaintext to be a multiple of 16 bytes
		String paddedPlaintext = padPlaintext(plaintext);
		byte[] paddedPlaintextBytes = paddedPlaintext.getBytes(StandardCharsets.UTF_8);

		if (mode.equalsIgnoreCase("ECB")) {
			for (int i = 0; i < paddedPlaintextBytes.length; i += 16) {
				byte[] block = Arrays.copyOfRange(paddedPlaintextBytes, i, i + 16);
				byte[] encryptedBlock = encryptBlock(block, roundKeys);
				cipherTextBuilder.append(Base64.getEncoder().encodeToString(encryptedBlock));
				cipherTextBuilder.append("\n");
			}
		} else if (mode.equalsIgnoreCase("CBC")) {
			byte[] iv = new byte[16];
			SECURE_RANDOM.nextBytes(iv);
			byte[] previousBlock = iv;
			cipherTextBuilder.append(Base64.getEncoder().encodeToString(iv)).append("\n");

			for (int i = 0; i < paddedPlaintextBytes.length; i += 16) {
				byte[] block = Arrays.copyOfRange(paddedPlaintextBytes, i, i + 16);
				block = xorBlocks(block, previousBlock);
				byte[] encryptedBlock = encryptBlock(block, roundKeys);
				previousBlock = encryptedBlock;
				cipherTextBuilder.append(Base64.getEncoder().encodeToString(encryptedBlock));
				cipherTextBuilder.append("\n");
			}

		} else {
			throw new AESException("Unsupported mode: " + mode);
		}

		writeToFile(cipherTextBuilder.toString().trim());

		return cipherTextBuilder.toString().trim(); // To ensure no extra whitespace
	}

	public String decrypt(String base64CipherText, byte[] key, String mode) throws AESException {
		byte[] roundKeys = expandKey(key);
		String[] cipherTextBlocks = base64CipherText.split("\\s+"); // Split on whitespace
		StringBuilder plainTextBuilder = new StringBuilder();

		if (mode.equalsIgnoreCase("ECB")) {
			for (String block : cipherTextBlocks) {
				try {
					byte[] cipherTextBytes = Base64.getDecoder().decode(block.trim()); // Clean and decode
					plainTextBuilder
							.append(new String(decryptBlock(cipherTextBytes, roundKeys), StandardCharsets.UTF_8));
				} catch (IllegalArgumentException e) {
					throw new AESException("Invalid Base64 input: " + block, e);
				}
			}
		} else if (mode.equalsIgnoreCase("CBC")) {
			if (cipherTextBlocks.length < 2) {
				throw new AESException("Cipher text too short for CBC mode");
			}

			byte[] iv = Base64.getDecoder().decode(cipherTextBlocks[0].trim());
			byte[] previousBlock = iv;

			for (int i = 1; i < cipherTextBlocks.length; i++) {
				try {
					byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBlocks[i].trim()); // Clean and decode
					byte[] decryptedBlock = decryptBlock(cipherTextBytes, roundKeys);
					decryptedBlock = xorBlocks(decryptedBlock, previousBlock);
					previousBlock = cipherTextBytes;
					plainTextBuilder.append(new String(decryptedBlock, StandardCharsets.UTF_8));
				} catch (IllegalArgumentException e) {
					throw new AESException("Invalid Base64 input: " + cipherTextBlocks[i], e);
				}
			}
		} else {
			throw new AESException("Unsupported mode: " + mode);
		}

		return removePadding(plainTextBuilder.toString());
	}

	private byte[] xorBlocks(byte[] block1, byte[] block2) {
		byte[] result = new byte[block1.length];
		for (int i = 0; i < block1.length; i++) {
			result[i] = (byte) (block1[i] ^ block2[i]);
		}
		return result;
	}

	private byte[] encryptBlock(byte[] plaintext, byte[] roundKeys) throws AESException {
		initialState = plaintext.clone();
		byte[] state = initialState;
		state = addRoundKey(state, Arrays.copyOfRange(roundKeys, 0, 16));
		for (int round = 1; round < NUMBEROFROUNDS; round++) {
			state = byteSub(state);
			byteSubState = state.clone();
			state = shiftRow(state);
			shiftRowState = state.clone();
			state = mixColumns(state);
			mixColumnsState = state.clone();
			state = addRoundKey(state, Arrays.copyOfRange(roundKeys, round * 16, (round + 1) * 16));
			roundKeyAddedState = state.clone();
		}
		state = byteSub(state);
		byteSubState = state.clone();
		state = shiftRow(state);
		shiftRowState = state.clone();
		state = addRoundKey(state, Arrays.copyOfRange(roundKeys, NUMBEROFROUNDS * 16, (NUMBEROFROUNDS + 1) * 16));
		finalState = state;
		return state;
	}

	private byte[] decryptBlock(byte[] ciphertext, byte[] roundKeys) throws AESException {
		initialState = ciphertext.clone();
		byte[] state = initialState;
		state = addRoundKey(state, Arrays.copyOfRange(roundKeys, NUMBEROFROUNDS * 16, (NUMBEROFROUNDS + 1) * 16));
		for (int round = NUMBEROFROUNDS - 1; round > 0; round--) {
			state = invShiftRow(state);
			shiftRowState = state.clone();
			state = invByteSub(state);
			byteSubState = state.clone();
			state = addRoundKey(state, Arrays.copyOfRange(roundKeys, round * 16, (round + 1) * 16));
			roundKeyAddedState = state.clone();
			state = invMixColumns(state);
			mixColumnsState = state.clone();
		}
		state = invShiftRow(state);
		shiftRowState = state.clone();
		state = invByteSub(state);
		byteSubState = state.clone();
		state = addRoundKey(state, Arrays.copyOfRange(roundKeys, 0, 16));
		finalState = state;
		return state;
	}

	public static byte[] invShiftRow(byte[] input) throws AESException {
		if (input == null || input.length != 16) {
			throw new AESException("Input must be a non-null array of length 16");
		}
		byte[][] state = new byte[4][4];
		for (int i = 0; i < 16; i++) {
			state[i % 4][i / 4] = input[i];
		}
		for (int i = 1; i < 4; i++) {
			state[i] = rightShift(state[i], i);
		}
		byte[] output = new byte[16];
		for (int i = 0; i < 16; i++) {
			output[i] = state[i % 4][i / 4];
		}
		return output;
	}

	private static void invMixColumn(byte[] column) {
		byte[] temp = new byte[4];
		temp[0] = (byte) (galoisMultiply((byte) 0x0e, column[0]) ^ galoisMultiply((byte) 0x0b, column[1])
				^ galoisMultiply((byte) 0x0d, column[2]) ^ galoisMultiply((byte) 0x09, column[3]));
		temp[1] = (byte) (galoisMultiply((byte) 0x09, column[0]) ^ galoisMultiply((byte) 0x0e, column[1])
				^ galoisMultiply((byte) 0x0b, column[2]) ^ galoisMultiply((byte) 0x0d, column[3]));
		temp[2] = (byte) (galoisMultiply((byte) 0x0d, column[0]) ^ galoisMultiply((byte) 0x09, column[1])
				^ galoisMultiply((byte) 0x0e, column[2]) ^ galoisMultiply((byte) 0x0b, column[3]));
		temp[3] = (byte) (galoisMultiply((byte) 0x0b, column[0]) ^ galoisMultiply((byte) 0x0d, column[1])
				^ galoisMultiply((byte) 0x09, column[2]) ^ galoisMultiply((byte) 0x0e, column[3]));
		System.arraycopy(temp, 0, column, 0, 4);
	}

	public static byte[] invByteSub(byte[] input) throws AESException {
		if (input == null) {
			throw new AESException("Input cannot be null");
		}
		byte[] output = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			int byteValue = input[i] & 0xFF;
			int row = (byteValue >> 4) & 0x0F;
			int col = byteValue & 0x0F;
			int invSBoxValue = getInvsbox()[(row << 4) | col];
			output[i] = (byte) invSBoxValue;
		}
		return output;
	}

	public static byte[] invMixColumns(byte[] input) throws AESException {
		if (input == null || input.length != 16) {
			throw new AESException("Input must be a non-null array of length 16");
		}
		byte[][] stateArray = new byte[4][4];
		for (int i = 0; i < 16; i++) {
			stateArray[i % 4][i / 4] = input[i];
		}

		for (int col = 0; col < 4; col++) {
			byte[] column = new byte[4];
			for (int row = 0; row < 4; row++) {
				column[row] = stateArray[row][col];
			}
			invMixColumn(column);
			for (int row = 0; row < 4; row++) {
				stateArray[row][col] = column[row];
			}
		}

		byte[] output = new byte[16];
		for (int i = 0; i < 16; i++) {
			output[i] = stateArray[i % 4][i / 4];
		}

		return output;
	}

	private static byte[] rightShift(byte[] row, int shift) {
		byte[] newRow = new byte[4];
		for (int i = 0; i < 4; i++) {
			newRow[(i + shift) % 4] = row[i];
		}
		return newRow;
	}

	public static String stringToHex(String str) {
		StringBuilder hex = new StringBuilder();

		for (char ch : str.toCharArray()) {
			hex.append(String.format("%02X", (int) ch));
		}
		return hex.toString();
	}

	String hexToASCII(String hex) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static byte[] fromHexString(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

	private static String readFromFile(String filename) {
		StringBuilder content = new StringBuilder();
		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()).append("\n"); // Keep the newlines for each line
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}
		return content.toString().trim(); // Remove the last newline added
	}

	private static String readFromFile() {
		StringBuilder content = new StringBuilder();
		try (Scanner scanner = new Scanner(new File("CipherText.txt"))) {
			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()).append("\n"); // Keep the newlines for each line
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}
		return content.toString().trim(); // Remove the last newline added
	}

	private static void writeToFile(String data) {
		try (FileWriter fileWriter = new FileWriter("CipherText.txt", false);
				PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.print(data); // Write the data as is, keeping newlines
		} catch (Exception e) {
			System.out.println("Failed to write to file: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			AESKeyGenerator g = new AESKeyGenerator();
			AES aes = new AES();

			byte[] generatedKey = g.generateAESKey("1", 256);

			String plainText = readFromFile("PlainText.txt");
			System.out.println("Original Plain Text: " + plainText);

			String encryptedASCII = aes.encrypt(plainText, generatedKey, "cbc");
			System.out.println("\nEncrypted Cipher in ASCII: " + encryptedASCII.replaceAll("\\s+", ""));

			String decryptedPlaintext = aes.decrypt(readFromFile(), generatedKey, "ecb");
			System.out.println("\nDecrypted Plain Text: " + decryptedPlaintext);
		} catch (AESException e) {
			System.out.println(e.getMessage());
		}
	}

	// Getters for attributes
	public byte[] getInitialState() {
		return initialState;
	}

	public byte[] getByteSubState() {
		return byteSubState;
	}

	public byte[] getShiftRowState() {
		return shiftRowState;
	}

	public byte[] getMixColumnsState() {
		return mixColumnsState;
	}

	public byte[] getRoundKeyAddedState() {
		return roundKeyAddedState;
	}

	public byte[] getFinalState() {
		return finalState;
	}

	public byte[] getRotWordState() {
		return rotWordState;
	}

	public byte[] getSubWordState() {
		return subWordState;
	}

	public byte getRconState() {
		return rconState;
	}
}
