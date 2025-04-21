package application;

import java.nio.charset.StandardCharsets;

public class Feistel {
    private static final int NUM_ROUNDS = 16;
    private static final int BLOCK_SIZE = 8; // 8 bytes = 64 bits

    private final long[] subKeys;

    public Feistel(long key) {
        this.subKeys = generateSubKeys(key);
    }

    private long[] generateSubKeys(long key) {
        long[] subKeys = new long[NUM_ROUNDS];
        for (int i = 0; i < NUM_ROUNDS; i++) {
            subKeys[i] = key ^ i; // Simple key schedule, XOR with round number
        }
        return subKeys;
    }

    public byte[] encrypt(byte[] plaintext) {
        int numBlocks = (int) Math.ceil((double) plaintext.length / BLOCK_SIZE);
        byte[] ciphertext = new byte[numBlocks * BLOCK_SIZE];

        for (int i = 0; i < numBlocks; i++) {
            long left = 0;
            long right = 0;
            for (int j = 0; j < BLOCK_SIZE / 2; j++) {
                left |= (long) (plaintext[i * BLOCK_SIZE + j] & 0xFF) << (8 * j);
                right |= (long) (plaintext[i * BLOCK_SIZE + j + BLOCK_SIZE / 2] & 0xFF) << (8 * j);
            }

            for (int round = 0; round < NUM_ROUNDS; round++) {
                long temp = left;
                left = right;
                right = temp ^ roundFunction(right, subKeys[round]);
            }

            for (int j = 0; j < BLOCK_SIZE / 2; j++) {
                ciphertext[i * BLOCK_SIZE + j] = (byte) ((right >> (8 * j)) & 0xFF);
                ciphertext[i * BLOCK_SIZE + j + BLOCK_SIZE / 2] = (byte) ((left >> (8 * j)) & 0xFF);
            }
        }

        return ciphertext;
    }
    

    public byte[] decrypt(byte[] ciphertext) {
        return encrypt(ciphertext); // Feistel cipher is symmetric
    }

    private long roundFunction(long data, long key) {
        // Simple round function, can be replaced with any suitable function
        return data + key;
    }

    public static void main(String[] args) {
        long key = 0x0123456789ABCDEFL;
        Feistel cipher = new Feistel(key);

        String plaintext = "Hello, Feistel Cipher!";
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = cipher.encrypt(plaintextBytes);
        System.out.println("Ciphertext: " + new String(ciphertext, StandardCharsets.UTF_8));

        byte[] decrypted = cipher.decrypt(ciphertext);
        System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }
}
