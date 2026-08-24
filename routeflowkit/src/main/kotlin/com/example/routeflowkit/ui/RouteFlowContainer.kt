package com.example.routeflowkit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.routeflowkit.action.RouteFlowAction
import com.example.routeflowkit.model.RouteFlowMode
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteProgress
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowCardPosition
import com.example.routeflowkit.style.RouteFlowLayout
import com.example.routeflowkit.style.RouteFlowMapPreset
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings
import com.example.routeflowkit.validation.RouteInputValidator
import com.example.routeflowkit.validation.ValidationResult

/**
 * Top-level orchestrating Composable for RouteFlowKit.
 *
 * Renders the underlying [RouteFlowMap] and overlays the appropriate card component
 * depending on [RouteFlowMode] and [RouteFlowUiState]. Dispatches interactions to [onAction].
 */
@Composable
internal fun RouteFlowContainer(
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
    val renderedUiState = if (enforceValidRoute) {
        validatedRouteState(mode, uiState, origin, destination, routeInfo, strings)
    } else {
        uiState
    }
    CompositionLocalProvider(
        LocalRouteFlowStyle provides style,
        LocalRouteFlowStrings provides strings,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            // Background Google Map
            RouteFlowMap(
                origin = origin,
                destination = destination,
                currentLocation = progress?.currentLocation ?: currentLocation,
                routeInfo = routeInfo,
                progressFraction = progress?.clampedProgressFraction,
                mapPreset = mapPreset,
                style = style,
                strings = strings,
                icons = icons,
                modifier = Modifier.fillMaxSize(),
            )

            // Overlay card positioned at the bottom of the map container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(layout.cardOuterPadding),
                contentAlignment = when (layout.cardPosition) {
                    RouteFlowCardPosition.Top -> Alignment.TopCenter
                    RouteFlowCardPosition.Center -> Alignment.Center
                    RouteFlowCardPosition.Bottom -> Alignment.BottomCenter
                },
            ) {
                if (renderedUiState !is RouteFlowUiState.Ready) {
                    RouteFlowMessageCard(
                        uiState = renderedUiState,
                        edgeCaseCode = edgeCaseCode,
                        onRetry = { onAction(RouteFlowAction.Retry) },
                        onGrantPermission = { onAction(RouteFlowAction.GrantPermission) },
                        onEnableLocation = { onAction(RouteFlowAction.EnableLocationServices) },
                        icons = icons,
                        isMissingIconTest = isMissingIconTest,
                        showRecoveryAction = layout.showMessageActions,
                    )
                } else {
                    when (mode) {
                        RouteFlowMode.DestinationSelection -> {
                            DestinationPickerCard(
                                presetDestinations = presetDestinations,
                                onDestinationSelected = { dest ->
                                    onAction(RouteFlowAction.SelectDestination(dest))
                                },
                                isMissingIconTest = isMissingIconTest,
                                customIcon = icons.destinationPicker,
                                searchIcon = icons.destinationSearch,
                                destinationIcon = icons.destinationItem,
                                selectedDestination = destination,
                            )
                        }

                        RouteFlowMode.RoutePreview -> {
                            if (destination != null) {
                                RouteBottomCard(
                                    content = RouteBottomCardContent(
                                        title = strings.routePreviewTitle,
                                        subtitle = status ?: "${origin?.label ?: strings.currentLocationLabel} → ${destination.label}",
                                        eta = routeInfo?.formattedDuration(strings),
                                        distance = routeInfo?.formattedDistance(strings),
                                        secondaryInfo = destination.label,
                                        primaryActionText = strings.confirmRouteButton,
                                        secondaryActionText = strings.backButton,
                                        icon = if (isMissingIconTest) null
                                        else icons.routePreview ?: RouteFlowDefaults.icons.routePreview,
                                    ),
                                    onPrimaryAction = { onAction(RouteFlowAction.ConfirmRoute(origin, destination)) },
                                    onSecondaryAction = { onAction(RouteFlowAction.CancelRide) },
                                )
                            } else {
                                RouteFlowMessageCard(
                                    uiState = RouteFlowUiState.DestinationRequired,
                                    onRetry = { onAction(RouteFlowAction.Retry) },
                                    icons = icons,
                                    isMissingIconTest = isMissingIconTest,
                                    showRecoveryAction = layout.showMessageActions,
                                )
                            }
                        }

                        RouteFlowMode.ActiveRide -> {
                            if (destination != null) {
                                RouteBottomCard(
                                    content = RouteBottomCardContent(
                                        title = strings.activeRideTitle,
                                        subtitle = progress?.status ?: status ?: strings.activeRideStatus,
                                        eta = progress?.remainingEta ?: routeInfo?.formattedDuration(strings),
                                        distance = progress?.remainingDistance ?: routeInfo?.formattedDistance(strings),
                                        secondaryInfo = destination.label,
                                        primaryActionText = strings.simulateArrivalButton,
                                        secondaryActionText = strings.cancelButton,
                                        icon = icons.activeRide.takeUnless { isMissingIconTest },
                                    ),
                                    onPrimaryAction = { onAction(RouteFlowAction.FinishRide) },
                                    onSecondaryAction = { onAction(RouteFlowAction.CancelRide) },
                                )
                            }
                        }

                        RouteFlowMode.Arrived -> {
                            RouteBottomCard(
                                content = RouteBottomCardContent(
                                    title = strings.arrivedTitle,
                                    subtitle = strings.arrivedSubtitle,
                                    secondaryInfo = destination?.label,
                                    primaryActionText = strings.doneButton,
                                    icon = icons.arrived.takeUnless { isMissingIconTest },
                                ),
                                onPrimaryAction = { onAction(RouteFlowAction.ResetFlow) },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun validatedRouteState(
    mode: RouteFlowMode,
    uiState: RouteFlowUiState,
    origin: RouteWaypoint?,
    destination: RouteWaypoint?,
    routeInfo: RouteInfo?,
    strings: RouteFlowStrings,
): RouteFlowUiState {
    if (uiState !is RouteFlowUiState.Ready ||
        mode !in setOf(RouteFlowMode.RoutePreview, RouteFlowMode.ActiveRide)
    ) return uiState
    if (destination == null) return RouteFlowUiState.DestinationRequired
    if (origin == null) return RouteFlowUiState.Error(strings.routeOriginRequiredMessage)
    if (routeInfo == null || routeInfo.polylinePoints.size < 2) {
        return RouteFlowUiState.RouteUnavailable(
            origin = origin.location,
            destination = destination.location,
            reason = strings.renderableRouteRequiredMessage,
        )
    }
    val endpointResult = RouteInputValidator.validateOriginDestination(
        origin.location,
        destination.location,
    )
    if (endpointResult is ValidationResult.Invalid) {
        return RouteFlowUiState.RouteUnavailable(
            origin = origin.location,
            destination = destination.location,
            reason = endpointResult.reason,
        )
    }
    routeInfo.polylinePoints.forEach { coordinate ->
        val result = RouteInputValidator.validateCoordinate(coordinate)
        if (result is ValidationResult.Invalid) {
            return RouteFlowUiState.RouteUnavailable(
                origin = origin.location,
                destination = destination.location,
                reason = result.reason,
            )
        }
    }
    return uiState
}

private fun RouteInfo.formattedDuration(strings: RouteFlowStrings): String =
    strings.estimatedTimeFormat.format((durationSeconds / 60).coerceAtLeast(1))

private fun RouteInfo.formattedDistance(strings: RouteFlowStrings): String =
    strings.distanceFormat.format(distanceMeters / 1_000.0)
