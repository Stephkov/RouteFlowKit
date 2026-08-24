package com.example.routeflowkit.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Styling contract for RouteFlowKit UI components, controlling colors,
 * shapes, spacing grid, and card elevations across host themes.
 */
@Immutable
data class RouteFlowStyle(
    val primaryColor: Color = Color(0xFF00F2FE),
    val secondaryColor: Color = Color(0xFF4FACFE),
    val backgroundColor: Color = Color(0xFF1E1E2C),
    val surfaceColor: Color = Color(0xFF2D2D44),
    val onSurfaceColor: Color = Color(0xFFFFFFFF),
    val subtitleColor: Color = Color(0xFFA0A0C0),
    val errorColor: Color = Color(0xFFFF5252),
    val successColor: Color = Color(0xFF00E676),
    val handleColor: Color = Color(0x40FFFFFF),
    val routeColor: Color = primaryColor,
    val routeWidth: Float = 10f,
    val completedRouteColor: Color = primaryColor,
    val remainingRouteColor: Color = routeColor.copy(alpha = 0.38f),
    val completedRouteWidth: Float = routeWidth,
    val primaryButtonContentColor: Color = Color.White,
    val mapControlColor: Color = surfaceColor,
    val cardCornerRadius: Dp = 16.dp,
    val cardElevation: Dp = 8.dp,
    val buttonHeight: Dp = 48.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
) {
    companion object {
        /** Theme 1: Dark Slate with Neon Cyan accent. */
        val TechSlateDark = RouteFlowStyle(
            primaryColor = Color(0xFF00E5FF),
            secondaryColor = Color(0xFF2979FF),
            backgroundColor = Color(0xFF12121A),
            surfaceColor = Color(0xFF1E1E2E),
            onSurfaceColor = Color(0xFFF1F5F9),
            subtitleColor = Color(0xFF94A3B8),
            errorColor = Color(0xFFFF5252),
            successColor = Color(0xFF00E676),
            handleColor = Color(0x40FFFFFF),
            cardCornerRadius = 16.dp,
            cardElevation = 8.dp,
            buttonHeight = 48.dp,
        )

        /** Theme 2: Minimal Light with Warm Emerald accent. */
        val EmeraldCleanLight = RouteFlowStyle(
            primaryColor = Color(0xFF0F9D58),
            secondaryColor = Color(0xFF00796B),
            backgroundColor = Color(0xFFF8FAF9),
            surfaceColor = Color(0xFFFFFFFF),
            onSurfaceColor = Color(0xFF1F2937),
            subtitleColor = Color(0xFF6B7280),
            errorColor = Color(0xFFDC2626),
            successColor = Color(0xFF16A34A),
            handleColor = Color(0x33000000),
            cardCornerRadius = 24.dp,
            cardElevation = 4.dp,
            buttonHeight = 48.dp,
        )
    }
}

/** Composition-local RouteFlowKit styling used by independently composed UI components. */
val LocalRouteFlowStyle = staticCompositionLocalOf { RouteFlowStyle() }
