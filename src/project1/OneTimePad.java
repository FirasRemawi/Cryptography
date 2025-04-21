package project1;

import java.util.Base64;

public class OneTimePad {
	private String base64Key;
	private byte[] key;

	public String getBase64Key() {
		return base64Key;
	}

	public void setBase64Key(String base64Key) {
		this.base64Key = base64Key;
		this.key = Base64.getDecoder().decode(base64Key);
	}

	public byte[] getLastUsedKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
		this.base64Key = Base64.getEncoder().encodeToString(key);
	}

	public String encrypt(String plaintext, byte[] key) {
		byte[] plaintextBytes = plaintext.getBytes();
		byte[] encrypted = new byte[plaintextBytes.length];
		this.key = key; // Save the key used for encryption

		for (int i = 0; i < plaintextBytes.length; i++) {
			encrypted[i] = (byte) (plaintextBytes[i] ^ key[i % key.length]);
		}
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String decrypt(String base64Ciphertext, byte[] key) {
		byte[] ciphertext = Base64.getDecoder().decode(base64Ciphertext);
		byte[] decrypted = new byte[ciphertext.length];

		for (int i = 0; i < ciphertext.length; i++) {
			decrypted[i] = (byte) (ciphertext[i] ^ key[i % key.length]);
		}

		return new String(decrypted);
	}

}
