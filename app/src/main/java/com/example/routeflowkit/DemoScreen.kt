package com.example.routeflowkit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.routeflowkit.action.RouteFlowAction
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteFlowData
import com.example.routeflowkit.model.RouteFlowMode
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteProgress
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowMapPreset
import com.example.routeflowkit.style.RouteFlowStrings
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.ui.RouteFlowMapScreen
import kotlinx.coroutines.delay
import kotlin.math.floor

private val flagshipOrigin = RouteWaypoint(
    label = "Rothschild Boulevard",
    location = GeoCoordinate(32.0639, 34.7731),
)

private data class FlagshipRoute(
    val destination: RouteWaypoint,
    val points: List<GeoCoordinate>,
    val distanceMeters: Double,
    val durationSeconds: Long,
    val summary: String,
)

private val flagshipRoutes = listOf(
    FlagshipRoute(
        destination = RouteWaypoint("Yarkon Park", GeoCoordinate(32.1017, 34.8114)),
        points = listOf(
            flagshipOrigin.location,
            GeoCoordinate(32.0718, 34.7814),
            GeoCoordinate(32.0810, 34.7898),
            GeoCoordinate(32.0930, 34.8038),
            GeoCoordinate(32.1017, 34.8114),
        ),
        distanceMeters = 5_800.0,
        durationSeconds = 840L,
        summary = "A calm, direct route through central Tel Aviv",
    ),
    FlagshipRoute(
        destination = RouteWaypoint("Tel Aviv Museum of Art", GeoCoordinate(32.0776, 34.7860)),
        points = listOf(
            flagshipOrigin.location,
            GeoCoordinate(32.0680, 34.7772),
            GeoCoordinate(32.0722, 34.7811),
            GeoCoordinate(32.0776, 34.7860),
        ),
        distanceMeters = 2_400.0,
        durationSeconds = 420L,
        summary = "A quick route to the cultural district",
    ),
    FlagshipRoute(
        destination = RouteWaypoint("Gordon Beach", GeoCoordinate(32.0827, 34.7681)),
        points = listOf(
            flagshipOrigin.location,
            GeoCoordinate(32.0696, 34.7715),
            GeoCoordinate(32.0758, 34.7698),
            GeoCoordinate(32.0827, 34.7681),
        ),
        distanceMeters = 3_100.0,
        durationSeconds = 540L,
        summary = "A relaxed route toward the waterfront",
    ),
)

@Composable
fun DemoScreen() {
    var showDeveloperShowcase by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf(RouteFlowMode.DestinationSelection) }
    var selectedRoute by remember { mutableStateOf<FlagshipRoute?>(null) }
    var selectedMapPreset by remember { mutableStateOf(RouteFlowMapPreset.Minimal) }
    var progressFraction by remember { mutableFloatStateOf(0f) }

    if (showDeveloperShowcase) {
        DeveloperShowcaseScreen(onClose = { showDeveloperShowcase = false })
        return
    }

    val strings = remember {
        RouteFlowStrings(
            selectDestinationTitle = "Where are you going?",
            destinationInputPlaceholder = "Choose a saved destination",
            originLabel = "Starting point",
            destinationLabel = "Destination",
            routePreviewTitle = "Your route is ready",
            confirmRouteButton = "Start route",
            backButton = "Choose another destination",
            activeRideTitle = "Enjoy your route",
            activeRideStatus = "On route",
            simulateArrivalButton = "Finish route",
            cancelButton = "End route",
            arrivedTitle = "You've arrived",
            arrivedSubtitle = "Your destination is just ahead. Enjoy your time.",
            doneButton = "Plan another route",
            estimatedTimeLabel = "Time remaining",
            currentLocationLabel = "Your position",
        )
    }
    val style = remember {
        RouteFlowStyle.EmeraldCleanLight.copy(
            primaryColor = Color(0xFF176BEB),
            secondaryColor = Color(0xFF1156B8),
            routeColor = Color(0xFF176BEB),
            completedRouteColor = Color(0xFF176BEB),
            remainingRouteColor = Color(0xFF9CB6D8),
            routeWidth = 8f,
            completedRouteWidth = 9f,
            backgroundColor = Color(0xFFF4F6F5),
            surfaceColor = Color.White,
            onSurfaceColor = Color(0xFF18201D),
            subtitleColor = Color(0xFF68746F),
            cardCornerRadius = 28.dp,
            cardElevation = 10.dp,
            buttonHeight = 52.dp,
        )
    }

    LaunchedEffect(mode) {
        if (mode != RouteFlowMode.ActiveRide) return@LaunchedEffect
        while (progressFraction < 1f && mode == RouteFlowMode.ActiveRide) {
            delay(90L)
            progressFraction = (progressFraction + 0.01f).coerceAtMost(1f)
        }
        if (progressFraction >= 1f && mode == RouteFlowMode.ActiveRide) {
            delay(500L)
            mode = RouteFlowMode.Arrived
        }
    }

    val activeRoute = selectedRoute
    val currentLocation = coordinateAtProgress(
        activeRoute?.points.orEmpty(),
        progressFraction,
    )
    val remainingFactor = 1f - progressFraction
    val progress = if (mode == RouteFlowMode.ActiveRide || mode == RouteFlowMode.Arrived) {
        RouteProgress(
            currentLocation = currentLocation,
            progressFraction = progressFraction,
            remainingDistance = "%.1f km".format((activeRoute?.distanceMeters ?: 0.0) * remainingFactor / 1_000.0),
            remainingEta = "${(((activeRoute?.durationSeconds ?: 0L) * remainingFactor) / 60).toInt()} min",
            status = when {
                progressFraction < 0.2f -> "Leaving the city centre"
                progressFraction < 0.65f -> "On route · Traffic is light"
                progressFraction < 1f -> "${activeRoute?.destination?.label.orEmpty()} is coming up"
                else -> "Route complete"
            },
        )
    } else null

    Box(Modifier.fillMaxSize()) {
        RouteFlowMapScreen(
            mode = mode,
            uiState = RouteFlowUiState.Ready(),
            presetDestinations = flagshipRoutes.map(FlagshipRoute::destination),
            data = RouteFlowData(
                origin = flagshipOrigin,
                destination = activeRoute?.destination,
                routeInfo = activeRoute?.takeIf { mode != RouteFlowMode.DestinationSelection }?.let { route ->
                    RouteInfo(
                        polylinePoints = route.points,
                        distanceMeters = route.distanceMeters,
                        durationSeconds = route.durationSeconds,
                    )
                },
                currentLocation = flagshipOrigin.location,
                status = if (mode == RouteFlowMode.RoutePreview) {
                    activeRoute?.summary
                } else null,
            ),
            progress = progress,
            mapPreset = selectedMapPreset,
            style = style,
            strings = strings,
            icons = RouteFlowIcons(
                destinationPicker = Icons.Default.Place,
                routePreview = Icons.Default.Place,
                activeRide = Icons.Default.Place,
                arrived = Icons.Default.CheckCircle,
                startMarkerResourceId = R.drawable.route_marker_start,
                destinationMarkerResourceId = R.drawable.route_marker_destination,
                currentLocationMarkerResourceId = R.drawable.route_marker_current,
            ),
            onAction = { action ->
                when (action) {
                    is RouteFlowAction.SelectDestination -> {
                        selectedRoute = flagshipRoutes.firstOrNull { it.destination == action.destination }
                        progressFraction = 0f
                    }
                    is RouteFlowAction.ConfirmRoute -> {
                        progressFraction = 0f
                        mode = RouteFlowMode.ActiveRide
                    }
                    RouteFlowAction.FinishRide -> {
                        progressFraction = 1f
                        mode = RouteFlowMode.Arrived
                    }
                    RouteFlowAction.CancelRide -> {
                        progressFraction = 0f
                        mode = if (mode == RouteFlowMode.ActiveRide) {
                            RouteFlowMode.RoutePreview
                        } else {
                            selectedRoute = null
                            RouteFlowMode.DestinationSelection
                        }
                    }
                    RouteFlowAction.ResetFlow -> {
                        selectedRoute = null
                        progressFraction = 0f
                        mode = RouteFlowMode.DestinationSelection
                    }
                    else -> Unit
                }
            },
        )

        FlagshipTopOverlay(
            mode = mode,
            status = progress?.status,
            strings = strings,
            onBack = {
                progressFraction = 0f
                if (mode == RouteFlowMode.RoutePreview) {
                    mode = RouteFlowMode.DestinationSelection
                } else {
                    mode = RouteFlowMode.RoutePreview
                }
            },
            onEndRoute = {
                progressFraction = 1f
                mode = RouteFlowMode.Arrived
            },
            onOpenDeveloperShowcase = { showDeveloperShowcase = true },
            selectedMapPreset = selectedMapPreset,
            onMapPresetSelected = { selectedMapPreset = it },
            canPreviewRoute = selectedRoute != null,
            onPreviewRoute = {
                progressFraction = 0f
                mode = RouteFlowMode.RoutePreview
            },
        )
    }
}

@Composable
private fun FlagshipTopOverlay(
    mode: RouteFlowMode,
    status: String?,
    strings: RouteFlowStrings,
    onBack: () -> Unit,
    onEndRoute: () -> Unit,
    onOpenDeveloperShowcase: () -> Unit,
    selectedMapPreset: RouteFlowMapPreset,
    onMapPresetSelected: (RouteFlowMapPreset) -> Unit,
    canPreviewRoute: Boolean,
    onPreviewRoute: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.96f),
        shadowElevation = 6.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
            if (mode != RouteFlowMode.DestinationSelection) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = strings.backButton)
                }
            } else {
                Surface(shape = CircleShape, color = Color(0xFF176BEB), modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp))
                }
            }
            Column(Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = when (mode) {
                        RouteFlowMode.DestinationSelection -> "RouteFlow"
                        RouteFlowMode.RoutePreview -> strings.routePreviewTitle
                        RouteFlowMode.ActiveRide -> strings.activeRideTitle
                        RouteFlowMode.Arrived -> strings.arrivedTitle
                    },
                    color = Color(0xFF18201D),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = status ?: when (mode) {
                        RouteFlowMode.DestinationSelection -> strings.selectDestinationTitle
                        RouteFlowMode.RoutePreview -> "Preview"
                        RouteFlowMode.ActiveRide -> strings.activeRideStatus
                        RouteFlowMode.Arrived -> "Route complete"
                    },
                    color = Color(0xFF68746F),
                    fontSize = 12.sp,
                    maxLines = 1,
                )
            }
            Spacer(Modifier.weight(1f))
            if (mode == RouteFlowMode.ActiveRide) {
                TextButton(onClick = onEndRoute) {
                    Text(strings.cancelButton, color = Color(0xFF18201D), fontWeight = FontWeight.SemiBold)
                }
            }
                androidx.compose.foundation.layout.Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Developer Showcase") },
                        onClick = {
                            menuExpanded = false
                            onOpenDeveloperShowcase()
                        },
                    )
                }
                }
            }
            if (mode == RouteFlowMode.DestinationSelection) {
                Text(
                    text = "Map appearance",
                    modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                    color = Color(0xFF68746F),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(modifier = Modifier.padding(horizontal = 6.dp)) {
                    RouteFlowMapPreset.entries.forEach { preset ->
                        FilterChip(
                            selected = selectedMapPreset == preset,
                            onClick = { onMapPresetSelected(preset) },
                            label = { Text(preset.name, fontSize = 11.sp) },
                            modifier = Modifier.padding(horizontal = 3.dp),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onPreviewRoute, enabled = canPreviewRoute) {
                        Text("Preview route", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/** Demo-only interpolation; the library receives snapshots and never calculates progress. */
private fun coordinateAtProgress(route: List<GeoCoordinate>, progress: Float): GeoCoordinate {
    if (route.isEmpty()) return GeoCoordinate(0.0, 0.0)
    if (route.size == 1) return route.first()
    val position = progress.coerceIn(0f, 1f) * route.lastIndex
    val startIndex = floor(position).toInt().coerceIn(0, route.lastIndex)
    val endIndex = (startIndex + 1).coerceAtMost(route.lastIndex)
    val segmentProgress = position - startIndex
    val start = route[startIndex]
    val end = route[endIndex]
    return GeoCoordinate(
        latitude = start.latitude + (end.latitude - start.latitude) * segmentProgress,
        longitude = start.longitude + (end.longitude - start.longitude) * segmentProgress,
    )
}
