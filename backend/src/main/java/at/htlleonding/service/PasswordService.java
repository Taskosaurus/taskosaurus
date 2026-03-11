package at.htlleonding.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles password hashing on the backend side.
 *
 * Security flow:
 * 1. Android sends SHA-256(plaintext password) — no plaintext ever travels over the network
 * 2. Backend receives the SHA-256 hash and applies BCrypt (which internally generates a random salt)
 * 3. Only the BCrypt hash is stored in the database
 *
 * This double-hashing approach ensures:
 * - No plaintext password is transmitted
 * - No plaintext password is stored
 * - Even if the database is leaked, passwords cannot be reversed due to BCrypt's one-way nature
 * - Each user gets a unique salt (BCrypt handles this automatically)
 */
@ApplicationScoped
public class PasswordService {

    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Hashes an incoming SHA-256 pre-hashed password with BCrypt.
     * BCrypt.hashpw internally generates a cryptographically random salt.
     *
     * @param sha256Password the SHA-256 hex string received from the Android client
     * @return the BCrypt hash to store in the database (includes the salt)
     */
    public String hashPassword(String sha256Password) {
        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        return BCrypt.hashpw(sha256Password, salt);
    }

    /**
     * Verifies a login attempt.
     *
     * @param sha256Password the SHA-256 hex string received from the Android client
     * @param storedBcryptHash the BCrypt hash stored in the database
     * @return true if the password matches
     */
    public boolean verifyPassword(String sha256Password, String storedBcryptHash) {
        return BCrypt.checkpw(sha256Password, storedBcryptHash);
    }
}
