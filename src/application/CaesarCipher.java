package application;

public class CaesarCipher implements Algorithms {
	private String input;
	private String output;
	private int key = 3;

	public CaesarCipher() {
		setKey(3);
	}

	@Override
	public String encrypt(String input, int key) {
		StringBuilder encryptedText = new StringBuilder();
		int shift = key; // Starting shift value

		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (Character.isLetter(ch)) {
				char base = Character.isUpperCase(ch) ? 'A' : 'a';
				char shifted = (char) ((ch - base + shift) % 26 + base);
				encryptedText.append(shifted);
				shift++; // Increment shift for the next letter
			} else {
				encryptedText.append(ch); // Non-letter characters are not changed
			}
		}

		return encryptedText.toString();
	}

	@Override
	public String decrypt(String output, int key) {
		StringBuilder decryptedText = new StringBuilder();
		int shift = key; // Starting shift value

		for (int i = 0; i < output.length(); i++) {
			char ch = output.charAt(i);
			if (Character.isLetter(ch)) {
				char base = Character.isUpperCase(ch) ? 'A' : 'a';
				int decryptShift = 26 - (shift % 26); // Calculate the reverse shift
				char shifted = (char) ((ch - base + decryptShift) % 26 + base);
				decryptedText.append(shifted);
				shift++; // Increment shift for the next letter, as was done in encryption
			} else {
				decryptedText.append(ch); // Non-letter characters are not changed
			}
		}

		return decryptedText.toString().toUpperCase();
	}

	private StringBuilder bruteForce(String encryptedText) {
		// Since the brute force method here is demonstration, we consider trying
		// all possible initial shifts from 0 to 25. The increment pattern (i.e., +1 per
		// letter) is a given.
		StringBuilder result = new StringBuilder();

		for (int initialShift = 1; initialShift <= 26; initialShift++) {
			StringBuilder decryptedText = new StringBuilder();
			int currentShift = initialShift;

			for (int i = 0; i < encryptedText.length(); i++) {
				char ch = encryptedText.charAt(i);
				if (Character.isLetter(ch)) {
					char base = Character.isUpperCase(ch) ? 'A' : 'a';
					int decryptShift = 26 - (currentShift % 26); // Adjusting for the rotation
					char shifted = (char) (((ch - base + decryptShift) % 26) + base);
					decryptedText.append(shifted);
					currentShift++; // Increment the shift for the next letter
				} else {
					decryptedText.append(ch); // Non-letter characters are kept as is.
				}
			}
			result.append("Initial Shift ").append(initialShift).append(": ").append(decryptedText).append("\n");
		}
		return result;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		bruteForce(input);
		return "CaesarCipher input," + input + ", output," + output + ", key," + key;
	}
	

}
