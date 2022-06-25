package com.dkaera.mash.ui.onboarding.states

class TextState :
    TextFieldState(validator = ::isTextValid, errorFor = ::textValidationError)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun textValidationError(email: String): String {
    return "Text is empty"
}

private fun isTextValid(email: String): Boolean {
    return email.trim().isNotEmpty()
}