package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

private val previewDestination = RouteWaypoint(
    label = "A very long destination label used to verify truncation without overflowing its card",
    location = GeoCoordinate(32.0853, 34.7818),
)

@Preview(name = "EC-9 Long text", widthDp = 360, heightDp = 480)
@Composable
private fun LongTextPreview() {
    PreviewSurface {
        DestinationPickerCard(
            presetDestinations = listOf(previewDestination),
            onDestinationSelected = {},
        )
    }
}

@Preview(name = "EC-10 Missing icon", widthDp = 360, heightDp = 480)
@Composable
private fun MissingIconPreview() {
    PreviewSurface {
        DestinationPickerCard(
            presetDestinations = listOf(previewDestination),
            onDestinationSelected = {},
            isMissingIconTest = true,
        )
    }
}

@Preview(name = "EC-11 320dp screen", widthDp = 320, heightDp = 480)
@Composable
private fun SmallScreenPreview() {
    PreviewSurface {
        RoutePreviewCard(
            origin = null,
            destination = previewDestination,
            routeInfo = null,
            onConfirmRoute = {},
            onBackClicked = {},
        )
    }
}

@Preview(name = "EC-12 Hebrew RTL", locale = "he", widthDp = 360, heightDp = 480)
@Composable
private fun HebrewRtlPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        PreviewSurface {
            ArrivedCard(
                destination = previewDestination,
                onDoneClicked = {},
                strings = RouteFlowStrings.Hebrew,
            )
        }
    }
}

@Preview(name = "Active ride metrics", widthDp = 360, heightDp = 480)
@Composable
private fun ActiveRideMetricsPreview() {
    PreviewSurface {
        ActiveRideBottomCard(
            destination = previewDestination,
            routeInfo = RouteInfo(distanceMeters = 8_400.0, durationSeconds = 720L),
            status = "Driver nearby",
            onSimulateArrival = {},
            onCancelRide = {},
        )
    }
}

@Composable
private fun PreviewSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RouteFlowStyle.TechSlateDark.backgroundColor),
    ) {
        content()
    }
}
