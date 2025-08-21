package client.service

import client.api.AuthApi
import shared.protocol.Status

object AuthService {

    fun handleSignIn(userName: String, password: String): Pair<Boolean, String> {

        if (userName.isBlank() || password.isBlank()) {
            return false to "username or password is blank."
        }
        val response = AuthApi.signIn(userName, password)

        return when (response.status) {
            Status.SUCCESS -> true to "signed in successfully!"
            else -> false to (response.message ?: "Sign in failed")
        }

    }

    fun handleSignUp(userName: String, password: String, password2: String): Pair<Boolean, String> {

        if (userName.isBlank()) return false to "Username or password is blank!"

        if (password != password2) return false to "Passwords do not match"

        val (isStrong, why) = isStrongEnough(password)

        if (!isStrong) return false to why

        val response = AuthApi.signUp(userName, password)

        return when (response.status) {
            Status.SUCCESS -> true to "signed up successfully!"
            else -> false to (response.message ?: "Signup failed")
        }
    }

    private fun isStrongEnough(password: String): Pair<Boolean, String> {
        val commonPasswords =
            listOf("123456", "password", "123456789", "12345", "12345678", "qwerty", "abc123", "password1")

        if (password.length < 8) return false to "Password must be at least 8 characters long."
        if (!password.any { it.isLowerCase() }) return false to "Password must include a lowercase letter."
        if (!password.any { it.isUpperCase() }) return false to "Password must include an uppercase letter."
        if (!password.any { it.isDigit() }) return false to "Password must include a number."
        if (!password.any { it in "!@#$%^&*(),." })
            return false to "Password must include one of the following special characters: !@#$%^&*(),."
        val forbiddenChars = "?\":'{}|<>[]"
        if (password.any { it in forbiddenChars })
            return false to "Password cannot include one of the following special characters: ?:\"{}|<>[]'\"'\\."
        if (commonPasswords.contains(password)) return false to "Password is common."
        return true to "Password is strong."
    }
}