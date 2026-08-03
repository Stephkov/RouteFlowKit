package com.example.routeflowkit.validation

/**
 * Outcome of a single validation check.
 */
sealed class ValidationResult {
    data object Ok : ValidationResult()
    data class Invalid(val reason: String) : ValidationResult()
}
