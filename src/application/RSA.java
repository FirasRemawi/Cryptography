package application;

import java.nio.charset.StandardCharsets;

public class RSA {
	private int p;
	private int q;
	private int n;
	private int phi;
	private int e;
	private int d;
	private int k = 2; // A constant value used in key generation

	public RSA(int p, int q) {
		this.p = p;
		this.q = q;
		generateKeys();
	}

	// Function to calculate gcd
	public static int gcd(int a, int h) {
		int temp;
		while (true) {
			temp = a % h;
			if (temp == 0)
				return h;
			a = h;
			h = temp;
		}
	}

	// Method to generate public and private keys
	private void generateKeys() {
		// Calculate n = p * q
		n = p * q;

		// Calculate phi = (p-1) * (q-1)
		phi = (p - 1) * (q - 1);

		// Find e such that 1 < e < phi and gcd(e, phi) = 1
		e = 2;
		while (e < phi) {
			if (gcd(e, phi) == 1)
				break;
			else
				e++;
		}

		d = (1 + (k * phi)) / e;
	}

	// Method to encrypt a message
	public int encrypt(int msg) {
		return modPow(msg, e, n);
	}

	// Method to decrypt a message
	public int decrypt(int encrypted) {
		return modPow(encrypted, d, n);
	}

	// Method for modular exponentiation
	private int modPow(int base, int exponent, int modulus) {
		int result = 1;
		for (int i = 0; i < exponent; i++) {
			result = (result * base) % modulus;
		}
		return result;
	}

}
