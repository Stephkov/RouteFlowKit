package com.example.routeflowkit.state

import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteWaypoint

/**
 * Sealed hierarchy representing every state the route-flow UI
 * can be in.  Consumers `collect` a `StateFlow<RouteFlowUiState>`
 * and render accordingly.
 */
sealed interface RouteFlowUiState {

    /** Map is visible and idle — optional route overlay. */
    data class Ready(
        val origin: RouteWaypoint? = null,
        val destination: RouteWaypoint? = null,
        val routeInfo: RouteInfo? = null,
    ) : RouteFlowUiState

    /** A route or location request is in progress. */
    data object Loading : RouteFlowUiState

    /** The user must supply a destination before proceeding. */
    data object DestinationRequired : RouteFlowUiState

    /** No route could be found between the given points. */
    data class RouteUnavailable(
        val origin: GeoCoordinate,
        val destination: GeoCoordinate,
        val reason: String? = null,
    ) : RouteFlowUiState

    /** The app needs ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION. */
    data object LocationPermissionRequired : RouteFlowUiState

    /** Location services (GPS / network) are turned off on the device. */
    data object LocationServicesDisabled : RouteFlowUiState

    /** An unrecoverable error. */
    data class Error(
        val message: String,
        val cause: Throwable? = null,
    ) : RouteFlowUiState
}
