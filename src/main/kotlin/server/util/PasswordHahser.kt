package server.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHasher {
    fun generateSalt(): String {
        val saltBytes = ByteArray(16)
        SecureRandom().nextBytes(saltBytes)
        return Base64.getEncoder().encodeToString(saltBytes)
    }
    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        // Step 1: hash the plain password
        val firstHash = digest.digest(password.toByteArray(Charsets.UTF_8))
        // Step 2: append the salt to the first hash (as bytes)
        val salted = firstHash + salt.toByteArray(Charsets.UTF_8)
        // Step 3: hash again
        val finalHash = digest.digest(salted)
        // Step 4: encode to Base64
        return Base64.getEncoder().encodeToString(finalHash)
    }
    fun verifyPassword(inputPassword: String, storedSalt: String, storedPassword: String): Boolean {
        val hashInputPassword = hashPassword(inputPassword, storedSalt)
//        println("""
//            the fun got
//            inputPassword: $inputPassword
//            storedSalt: $storedSalt
//            storedPassword: $storedPassword
//
//            and the hashed password is $hashInputPassword
//        """.trimIndent())
        return hashInputPassword == storedPassword
    }

}

//fun main() {
//    val salt = PasswordHasher.generateSalt()
//    val passwordHashed = PasswordHasher.hashPassword("123", salt)
//
//    println("the random salt: $salt")
//    println("the password hashed: $passwordHashed")
//
//    print(verifyPassword("123", salt, passwordHashed))
//}