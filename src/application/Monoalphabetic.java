package application;

import java.util.ArrayList;
import java.util.Collections;

public class Monoalphabetic implements Algorithms {
	private ArrayList<Character> shuffledAlphabet;
	private static final ArrayList<Character> NORMAL_ALPHABET = new ArrayList<>();

	static {
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			NORMAL_ALPHABET.add(ch);
		}
	}

	public Monoalphabetic() {
		shuffledAlphabet = new ArrayList<>(NORMAL_ALPHABET);
		Collections.shuffle(shuffledAlphabet);
	}

	@Override
	public String encrypt(String input, int key) {
		return transform(input, NORMAL_ALPHABET, shuffledAlphabet);
	}

	@Override
	public String decrypt(String input, int key) {
		return transform(input, shuffledAlphabet, NORMAL_ALPHABET);
	}

	private String transform(String input, ArrayList<Character> sourceAlphabet, ArrayList<Character> targetAlphabet) {
		StringBuilder output = new StringBuilder();

		for (char ch : input.toUpperCase().toCharArray()) {
			int index = sourceAlphabet.indexOf(ch);
			if (index != -1) {
				output.append(targetAlphabet.get(index));
			} else {
				output.append(ch); // Non-alphabet characters are not modified
			}
		}

		return output.toString();
	}
}
