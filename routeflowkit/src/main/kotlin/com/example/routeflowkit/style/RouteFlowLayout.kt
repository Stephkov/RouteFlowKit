package com.example.routeflowkit.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Supported positions for the mode or message card over the map. */
enum class RouteFlowCardPosition {
    Top,
    Center,
    Bottom,
}

/**
 * Small layout contract for positioning and insetting RouteFlowKit's overlay card.
 * Component-internal spacing continues to come from [RouteFlowStyle].
 */
@Immutable
data class RouteFlowLayout(
    val cardPosition: RouteFlowCardPosition = RouteFlowCardPosition.Bottom,
    val cardOuterPadding: Dp = 0.dp,
    val showMessageActions: Boolean = true,
)
