package com.example.routeflowkit

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteFlowData
import com.example.routeflowkit.model.RouteFlowMode
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.style.RouteFlowCardPosition
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowLayout
import com.example.routeflowkit.style.RouteFlowMapPreset
import com.example.routeflowkit.style.RouteFlowStrings
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.ui.RouteFlowMapScreen

private enum class RobustnessExample(val label: String) {
    LongText("Long text"),
    MissingDestination("Missing destination"),
    MissingRoute("Missing route"),
    InvalidCoordinate("Invalid coordinate"),
    SameEndpoints("Same start and destination"),
    MissingIcon("Missing custom icon"),
    HebrewRtl("RTL / Hebrew"),
}

@Composable
internal fun DeveloperShowcaseScreen(onClose: () -> Unit) {
    var selectedPreset by remember { mutableStateOf(RouteFlowMapPreset.Default) }
    var example by remember { mutableStateOf(RobustnessExample.LongText) }

    val origin = remember { RouteWaypoint("City Library", GeoCoordinate(32.0808, 34.7806)) }
    val regularDestination = remember { RouteWaypoint("Riverside Gardens", GeoCoordinate(32.1054, 34.8091)) }
    val regularRoute = remember {
        listOf(
            origin.location,
            GeoCoordinate(32.0860, 34.7860),
            GeoCoordinate(32.0920, 34.7925),
            GeoCoordinate(32.0980, 34.8010),
            regularDestination.location,
        )
    }
    val destination = when (example) {
        RobustnessExample.MissingDestination -> null
        RobustnessExample.SameEndpoints -> RouteWaypoint("Same as start", origin.location)
        RobustnessExample.LongText -> regularDestination.copy(
            label = "Riverside Gardens — north entrance beside the restored pedestrian bridge and community pavilion",
        )
        else -> regularDestination
    }
    val routeInfo = when (example) {
        RobustnessExample.MissingRoute -> null
        RobustnessExample.InvalidCoordinate -> RouteInfo(
            polylinePoints = listOf(origin.location, GeoCoordinate(123.0, 34.79), regularDestination.location),
            distanceMeters = 4_200.0,
            durationSeconds = 720L,
        )
        else -> RouteInfo(
            polylinePoints = regularRoute,
            distanceMeters = 4_200.0,
            durationSeconds = 720L,
        )
    }
    val strings = when (example) {
        RobustnessExample.HebrewRtl -> RouteFlowStrings.Hebrew.copy(
            routePreviewTitle = "מסלול לדוגמה עם טקסט ארוך לבדיקת פריסה מימין לשמאל",
        )
        RobustnessExample.LongText -> RouteFlowStrings(
            routePreviewTitle = "Your carefully planned riverside route is ready to preview",
            confirmRouteButton = "Continue with this route",
        )
        else -> RouteFlowStrings(routePreviewTitle = "Developer route preview")
    }
    val isMessageScenario = example in setOf(
        RobustnessExample.MissingDestination,
        RobustnessExample.MissingRoute,
        RobustnessExample.InvalidCoordinate,
        RobustnessExample.SameEndpoints,
    )

    CompositionLocalProvider(
        LocalLayoutDirection provides if (example == RobustnessExample.HebrewRtl) {
            LayoutDirection.Rtl
        } else {
            LayoutDirection.Ltr
        },
    ) {
        Box(Modifier.fillMaxSize()) {
            RouteFlowMapScreen(
                mode = RouteFlowMode.RoutePreview,
                uiState = RouteFlowUiState.Ready(),
                presetDestinations = emptyList(),
                data = RouteFlowData(
                    origin = origin,
                    destination = destination,
                    routeInfo = routeInfo,
                    currentLocation = origin.location,
                    status = if (example == RobustnessExample.LongText) {
                        "A deliberately detailed status message that remains readable without covering the route or primary action"
                    } else {
                        "Same route · Configurable presentation"
                    },
                ),
                mapPreset = selectedPreset,
                style = RouteFlowStyle.EmeraldCleanLight.copy(
                    primaryColor = Color(0xFF176BEB),
                    routeColor = Color(0xFF176BEB),
                ),
                strings = strings,
                icons = if (example == RobustnessExample.MissingIcon) {
                    RouteFlowIcons(routePreview = null)
                } else {
                    RouteFlowIcons()
                },
                layout = RouteFlowLayout(
                    cardPosition = if (isMessageScenario) {
                        RouteFlowCardPosition.Center
                    } else {
                        RouteFlowCardPosition.Bottom
                    },
                    cardOuterPadding = if (isMessageScenario) 20.dp else 0.dp,
                    showMessageActions = !isMessageScenario,
                ),
                enforceValidRoute = true,
                onAction = {},
            )

            DeveloperControls(
                selectedPreset = selectedPreset,
                onPresetSelected = { selectedPreset = it },
                selectedExample = example,
                onExampleSelected = { example = it },
                onClose = onClose,
            )
        }
    }
}

@Composable
private fun DeveloperControls(
    selectedPreset: RouteFlowMapPreset,
    onPresetSelected: (RouteFlowMapPreset) -> Unit,
    selectedExample: RobustnessExample,
    onExampleSelected: (RobustnessExample) -> Unit,
    onClose: () -> Unit,
) {
    Surface(
        modifier = Modifier.statusBarsPadding().padding(12.dp).fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.97f),
        shadowElevation = 8.dp,
    ) {
        Column(Modifier.padding(vertical = 8.dp)) {
            Row(Modifier.padding(horizontal = 8.dp)) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("Developer Showcase", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("Reusable presentation and robust input handling", fontSize = 12.sp, color = Color(0xFF68746F))
                }
            }
            ShowcaseSectionLabel("Map presentation")
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp)) {
                RouteFlowMapPreset.entries.forEach { preset ->
                    FilterChip(
                        selected = selectedPreset == preset,
                        onClick = { onPresetSelected(preset) },
                        label = { Text(preset.name) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
            ShowcaseSectionLabel("Robustness / edge cases")
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp)) {
                RobustnessExample.entries.forEach { item ->
                    FilterChip(
                        selected = selectedExample == item,
                        onClick = { onExampleSelected(item) },
                        label = { Text(item.label) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowcaseSectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
        color = Color(0xFF68746F),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    )
}
