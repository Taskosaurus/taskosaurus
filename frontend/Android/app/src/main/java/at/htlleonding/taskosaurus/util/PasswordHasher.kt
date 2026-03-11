package at.htlleonding.taskosaurus.util

import java.security.MessageDigest

object PasswordHasher {
    /**
     * Hashes the password with SHA-256 before sending to the server.
     * The server will then apply an additional bcrypt hash with a salt.
     * This ensures no plaintext password is ever transmitted over the network.
     */
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
