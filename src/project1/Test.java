package project1;

import java.util.Base64;

public class Test {
	public static byte[] main(String[] args) {
		String base64Str = "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\r\n"
				+ "";

		 int requiredLength = 4 - (base64Str.length() % 4);
		    requiredLength = requiredLength == 4 ? 0 : requiredLength; // Normalize to 0 if it's 4.
		    String paddedBase64Str = base64Str + "=".repeat(requiredLength);

		    try {
		        return Base64.getDecoder().decode(paddedBase64Str);
		    } catch (IllegalArgumentException e) {
		        System.err.println("Failed to decode Base64 data: " + e.getMessage());
		        return null; // Handle this error appropriately.
		    }
	}
}
