package com.example.routeflowkit.model

/**
 * Describes a calculated route (stub for Week 2 — fields are
 * always empty / zero until route calculation is implemented).
 */
data class RouteInfo(
    val polylinePoints: List<GeoCoordinate> = emptyList(),
    val distanceMeters: Double = 0.0,
    val durationSeconds: Long = 0L,
)
