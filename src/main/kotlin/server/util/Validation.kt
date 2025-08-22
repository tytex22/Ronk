package server.util

object Validation {

    fun isStrongEnough(password: String): Pair<Boolean, String> {
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