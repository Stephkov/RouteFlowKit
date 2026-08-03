package com.example.routeflowkit.model

/**
 * A labelled point along a route.
 *
 * @property label    Human-readable name (e.g. "Home", "Office").
 * @property location The geographic position.
 */
data class RouteWaypoint(
    val label: String,
    val location: GeoCoordinate,
)
