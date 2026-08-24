package com.example.routeflowkit.model

/**
 * Provider-independent map data supplied by the host application.
 *
 * RouteFlowKit only presents this data. It does not retrieve the current location or
 * calculate route information.
 */
data class RouteFlowData(
    val origin: RouteWaypoint? = null,
    val destination: RouteWaypoint? = null,
    val routeInfo: RouteInfo? = null,
    val currentLocation: GeoCoordinate? = null,
    val status: String? = null,
)
