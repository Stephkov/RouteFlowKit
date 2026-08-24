package com.example.routeflowkit.validation

/**
 * Outcome of a single validation check.
 */
sealed class ValidationResult {
    /** The supplied route input satisfies all applicable validation rules. */
    data object Ok : ValidationResult()

    /** The supplied route input is invalid for the human-readable [reason]. */
    data class Invalid(val reason: String) : ValidationResult()
}
