package application;

public class KeyExpansion {

	public static void main(String[] args) {
		// Original 32-bit key (right half)
		String originalKey = "11001100110011001100110011001100"; // Example key

		// Expand the key
		String expandedKey = expandKey(originalKey);

		// Print the expanded key
		System.out.println("Original Key (32 bits): " + originalKey);
		System.out.println("Expanded Key (48 bits): " + expandedKey);
		System.out.println("Original key length: " + originalKey.length());
		System.out.println("Expanded key length: " + expandedKey.length());

	}

	public static String expandKey(String originalKey) {
		// Create the expansion permutation table as an array
		int[] expansionTable = expansionTable();
		StringBuilder expandedKeyBuilder = new StringBuilder();

		// Expand the original key using the table
		for (int i = 0; i < expansionTable.length; i++) {
			int index = expansionTable[i] - 1;
			char bit = originalKey.charAt(index);
			expandedKeyBuilder.append(bit); // Append the bit to the expanded key
		}

		// Convert StringBuilder to String
		String expandedKey = expandedKeyBuilder.toString();

		return expandedKey;
	}

	public static int[] expansionTable() {
		// E-permutation Box as mentioned in slides
		int[] table = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18,
				19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

		return table;
	}
}
