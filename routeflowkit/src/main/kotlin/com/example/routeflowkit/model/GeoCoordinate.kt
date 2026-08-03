package com.example.routeflowkit.model

/**
 * A geographic coordinate expressed in degrees.
 *
 * This is the library's public lat/lng type so that consumers never
 * need a compile-time dependency on Google Maps `LatLng`.
 *
 * @property latitude  Must be in [−90, 90].
 * @property longitude Must be in [−180, 180].
 */
data class GeoCoordinate(
    val latitude: Double,
    val longitude: Double,
)
