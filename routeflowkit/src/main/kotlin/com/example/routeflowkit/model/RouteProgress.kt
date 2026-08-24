package com.example.routeflowkit.model

import androidx.compose.runtime.Immutable
import kotlin.math.roundToInt

/**
 * A host-calculated snapshot of progress along a route.
 *
 * RouteFlowKit does not derive these values. It clamps [progressFraction] only for safe
 * presentation and renders the optional current location and display-ready text.
 */
@Immutable
data class RouteProgress(
    val currentLocation: GeoCoordinate? = null,
    val progressFraction: Float = 0f,
    val remainingDistance: String? = null,
    val remainingEta: String? = null,
    val status: String? = null,
) {
    val clampedProgressFraction: Float
        get() = progressFraction.takeIf(Float::isFinite)?.coerceIn(0f, 1f) ?: 0f
}

/** Returns a safe presentation split index, or null when no route point exists. */
internal fun routeProgressSplitIndex(pointCount: Int, progressFraction: Float): Int? {
    if (pointCount <= 0) return null
    val clamped = progressFraction.takeIf(Float::isFinite)?.coerceIn(0f, 1f) ?: 0f
    return (clamped * (pointCount - 1)).roundToInt().coerceIn(0, pointCount - 1)
}
