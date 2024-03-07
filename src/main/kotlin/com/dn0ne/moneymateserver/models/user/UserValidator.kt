package com.dn0ne.moneymateserver.models.user

object UserValidator {
    private const val EMAIL_REGEX_STRING = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
    private const val PASSWORD_REGEX_STRING = "^[^ ]{8,20}\$"

    fun validateUser(user: User): UserValidationResult {
        var result = UserValidationResult()

        if (user.email.isBlank() || !user.email.matches(EMAIL_REGEX_STRING.toRegex())) {
            result = result.copy(
                emailError = "Invalid email."
            )
        }

        if (user.password.isBlank() || !user.password.matches(PASSWORD_REGEX_STRING.toRegex())) {
            result = result.copy(
                passwordError = "Invalid password."
            )
        }

        return result
    }

    data class UserValidationResult(
        val emailError: String? = null,
        val passwordError: String? = null
    )
}