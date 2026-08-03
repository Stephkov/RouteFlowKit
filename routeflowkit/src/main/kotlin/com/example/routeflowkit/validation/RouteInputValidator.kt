package com.example.routeflowkit.validation

import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteWaypoint

/**
 * Pure-function validator for route-flow input data.
 *
 * Every public method returns [ValidationResult].  Edge cases
 * are documented inline and linked from the project README.
 *
 * ### Edge-case catalogue
 *
 * | ID   | Rule                                           |
 * |------|------------------------------------------------|
 * | EC-1 | Latitude must be in [−90, 90]                  |
 * | EC-2 | Longitude must be in [−180, 180]               |
 * | EC-3 | Latitude/Longitude must not be NaN             |
 * | EC-4 | Latitude/Longitude must not be ±Infinity       |
 * | EC-5 | Waypoint label must not be blank               |
 * | EC-6 | Origin and destination must not be identical   |
 * | EC-7 | Waypoint list must contain ≥ 2 points          |
 * | EC-8 | Waypoint list must not contain duplicate coords |
 */
object RouteInputValidator {

    /** EC-1 / EC-2 / EC-3 / EC-4 */
    fun validateCoordinate(coord: GeoCoordinate): ValidationResult {
        if (coord.latitude.isNaN() || coord.longitude.isNaN())
            return ValidationResult.Invalid("Coordinate contains NaN (EC-3)")
        if (coord.latitude.isInfinite() || coord.longitude.isInfinite())
            return ValidationResult.Invalid("Coordinate contains Infinity (EC-4)")
        if (coord.latitude !in -90.0..90.0)
            return ValidationResult.Invalid(
                "Latitude ${coord.latitude} out of range [-90, 90] (EC-1)"
            )
        if (coord.longitude !in -180.0..180.0)
            return ValidationResult.Invalid(
                "Longitude ${coord.longitude} out of range [-180, 180] (EC-2)"
            )
        return ValidationResult.Ok
    }

    /** EC-5 — plus coordinate sub-validation. */
    fun validateWaypoint(waypoint: RouteWaypoint): ValidationResult {
        if (waypoint.label.isBlank())
            return ValidationResult.Invalid("Waypoint label is blank (EC-5)")
        return validateCoordinate(waypoint.location)
    }

    /** EC-6 */
    fun validateOriginDestination(
        origin: GeoCoordinate,
        destination: GeoCoordinate,
    ): ValidationResult {
        if (origin == destination)
            return ValidationResult.Invalid(
                "Origin and destination are identical (EC-6)"
            )
        return ValidationResult.Ok
    }

    /** EC-7 / EC-8 — plus per-waypoint sub-validation. */
    fun validateWaypointList(
        waypoints: List<RouteWaypoint>,
    ): ValidationResult {
        if (waypoints.size < 2)
            return ValidationResult.Invalid(
                "At least 2 waypoints required, got ${waypoints.size} (EC-7)"
            )
        for (wp in waypoints) {
            val r = validateWaypoint(wp)
            if (r is ValidationResult.Invalid) return r
        }
        val coords = waypoints.map { it.location }
        if (coords.toSet().size != coords.size)
            return ValidationResult.Invalid(
                "Waypoint list contains duplicate coordinates (EC-8)"
            )
        return ValidationResult.Ok
    }
}
