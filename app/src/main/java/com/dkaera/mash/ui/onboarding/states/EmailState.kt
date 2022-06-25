package com.dkaera.mash.ui.onboarding.states

import android.util.Patterns

class EmailState :
    TextFieldState(validator = ::isTextValid, errorFor = ::emailValidationError)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun emailValidationError(email: String): String {
    return "Invalid email: $email"
}

private fun isTextValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
