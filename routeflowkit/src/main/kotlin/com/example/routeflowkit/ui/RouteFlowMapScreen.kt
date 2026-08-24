package com.example.routeflowkit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.routeflowkit.action.RouteFlowAction
import com.example.routeflowkit.model.RouteFlowMode
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteFlowData
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteProgress
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowLayout
import com.example.routeflowkit.style.RouteFlowMapPreset
import com.example.routeflowkit.style.RouteFlowStrings
import com.example.routeflowkit.style.RouteFlowStyle

/**
 * Public RouteFlowKit screen facade. Host applications own state, supply location and route
 * data, and handle all actions. RouteFlowKit never retrieves the device location itself.
 */
@Composable
fun RouteFlowMapScreen(
    mode: RouteFlowMode,
    uiState: RouteFlowUiState,
    presetDestinations: List<RouteWaypoint>,
    onAction: (RouteFlowAction) -> Unit,
    modifier: Modifier = Modifier,
    origin: RouteWaypoint? = null,
    destination: RouteWaypoint? = null,
    routeInfo: RouteInfo? = null,
    currentLocation: GeoCoordinate? = null,
    status: String? = null,
    edgeCaseCode: String? = null,
    isMissingIconTest: Boolean = false,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icons: RouteFlowIcons = RouteFlowDefaults.icons,
    layout: RouteFlowLayout = RouteFlowLayout(),
    progress: RouteProgress? = null,
    mapPreset: RouteFlowMapPreset = RouteFlowMapPreset.Default,
    enforceValidRoute: Boolean = false,
) {
    RouteFlowContainer(
        mode = mode,
        uiState = uiState,
        presetDestinations = presetDestinations,
        onAction = onAction,
        modifier = modifier,
        origin = origin,
        destination = destination,
        routeInfo = routeInfo,
        currentLocation = currentLocation,
        status = status,
        edgeCaseCode = edgeCaseCode,
        isMissingIconTest = isMissingIconTest,
        style = style,
        strings = strings,
        icons = icons,
        layout = layout,
        progress = progress,
        mapPreset = mapPreset,
        enforceValidRoute = enforceValidRoute,
    )
}

/** Convenience overload accepting provider-independent map data as a single value. */
@Composable
fun RouteFlowMapScreen(
    mode: RouteFlowMode,
    uiState: RouteFlowUiState,
    presetDestinations: List<RouteWaypoint>,
    onAction: (RouteFlowAction) -> Unit,
    data: RouteFlowData,
    modifier: Modifier = Modifier,
    edgeCaseCode: String? = null,
    isMissingIconTest: Boolean = false,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icons: RouteFlowIcons = RouteFlowDefaults.icons,
    layout: RouteFlowLayout = RouteFlowLayout(),
    progress: RouteProgress? = null,
    mapPreset: RouteFlowMapPreset = RouteFlowMapPreset.Default,
    enforceValidRoute: Boolean = false,
) = RouteFlowMapScreen(
    mode = mode,
    uiState = uiState,
    presetDestinations = presetDestinations,
    onAction = onAction,
    modifier = modifier,
    origin = data.origin,
    destination = data.destination,
    routeInfo = data.routeInfo,
    currentLocation = data.currentLocation,
    status = data.status,
    edgeCaseCode = edgeCaseCode,
    isMissingIconTest = isMissingIconTest,
    style = style,
    strings = strings,
    icons = icons,
    layout = layout,
    progress = progress,
    mapPreset = mapPreset,
    enforceValidRoute = enforceValidRoute,
)
