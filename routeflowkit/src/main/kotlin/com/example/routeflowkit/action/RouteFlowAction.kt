package com.example.routeflowkit.action

import com.example.routeflowkit.model.RouteWaypoint

/**
 * Host-app callbacks triggered by user interactions within RouteFlowKit UI components.
 */
sealed interface RouteFlowAction {

    /** User chose a destination waypoint. */
    data class SelectDestination(val destination: RouteWaypoint) : RouteFlowAction

    /** User confirmed the selected route to start the ride. */
    data class ConfirmRoute(
        val origin: RouteWaypoint?,
        val destination: RouteWaypoint,
    ) : RouteFlowAction

    /** User cancelled an active ride or route preview. */
    data object CancelRide : RouteFlowAction

    /** User finished the ride or completed arrival screen. */
    data object FinishRide : RouteFlowAction

    /** User initiated a reset back to destination selection. */
    data object ResetFlow : RouteFlowAction

    /** User tapped a retry action for a failed state. */
    data object Retry : RouteFlowAction

    /** User requested to grant missing location permissions. */
    data object GrantPermission : RouteFlowAction

    /** User tapped to enable device location services. */
    data object EnableLocationServices : RouteFlowAction
}
