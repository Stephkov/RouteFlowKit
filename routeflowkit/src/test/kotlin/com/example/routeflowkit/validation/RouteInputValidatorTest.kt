package com.example.routeflowkit.validation

import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteWaypoint
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteInputValidatorTest {

    // ── Coordinate validation ────────────────────────────

    @Test
    fun `valid coordinate at origin`() {
        assertOk(RouteInputValidator.validateCoordinate(GeoCoordinate(0.0, 0.0)))
    }

    @Test
    fun `valid coordinate at boundary`() {
        assertOk(RouteInputValidator.validateCoordinate(GeoCoordinate(90.0, 180.0)))
        assertOk(RouteInputValidator.validateCoordinate(GeoCoordinate(-90.0, -180.0)))
    }

    // EC-1
    @Test
    fun `latitude above 90 is invalid`() {
        assertInvalid("EC-1", RouteInputValidator.validateCoordinate(GeoCoordinate(90.1, 0.0)))
    }

    @Test
    fun `latitude below -90 is invalid`() {
        assertInvalid("EC-1", RouteInputValidator.validateCoordinate(GeoCoordinate(-90.1, 0.0)))
    }

    // EC-2
    @Test
    fun `longitude above 180 is invalid`() {
        assertInvalid("EC-2", RouteInputValidator.validateCoordinate(GeoCoordinate(0.0, 180.1)))
    }

    @Test
    fun `longitude below -180 is invalid`() {
        assertInvalid("EC-2", RouteInputValidator.validateCoordinate(GeoCoordinate(0.0, -180.1)))
    }

    // EC-3
    @Test
    fun `NaN latitude is invalid`() {
        assertInvalid(
            "EC-3",
            RouteInputValidator.validateCoordinate(GeoCoordinate(Double.NaN, 0.0))
        )
    }

    @Test
    fun `NaN longitude is invalid`() {
        assertInvalid(
            "EC-3",
            RouteInputValidator.validateCoordinate(GeoCoordinate(0.0, Double.NaN))
        )
    }

    // EC-4
    @Test
    fun `positive infinity latitude is invalid`() {
        assertInvalid(
            "EC-4",
            RouteInputValidator.validateCoordinate(
                GeoCoordinate(Double.POSITIVE_INFINITY, 0.0)
            )
        )
    }

    @Test
    fun `negative infinity longitude is invalid`() {
        assertInvalid(
            "EC-4",
            RouteInputValidator.validateCoordinate(
                GeoCoordinate(0.0, Double.NEGATIVE_INFINITY)
            )
        )
    }

    // ── Waypoint validation ──────────────────────────────

    @Test
    fun `valid waypoint`() {
        assertOk(
            RouteInputValidator.validateWaypoint(
                RouteWaypoint("Home", GeoCoordinate(37.7749, -122.4194))
            )
        )
    }

    // EC-5
    @Test
    fun `blank label is invalid`() {
        assertInvalid(
            "EC-5",
            RouteInputValidator.validateWaypoint(
                RouteWaypoint("", GeoCoordinate(0.0, 0.0))
            )
        )
    }

    @Test
    fun `whitespace-only label is invalid`() {
        assertInvalid(
            "EC-5",
            RouteInputValidator.validateWaypoint(
                RouteWaypoint("   ", GeoCoordinate(0.0, 0.0))
            )
        )
    }

    @Test
    fun `waypoint with bad coordinate delegates error`() {
        assertInvalid(
            "EC-1",
            RouteInputValidator.validateWaypoint(
                RouteWaypoint("Bad", GeoCoordinate(999.0, 0.0))
            )
        )
    }

    // ── Origin / Destination ─────────────────────────────

    @Test
    fun `different origin and destination is valid`() {
        assertOk(
            RouteInputValidator.validateOriginDestination(
                GeoCoordinate(37.0, -122.0),
                GeoCoordinate(34.0, -118.0),
            )
        )
    }

    // EC-6
    @Test
    fun `identical origin and destination is invalid`() {
        val c = GeoCoordinate(37.7749, -122.4194)
        assertInvalid("EC-6", RouteInputValidator.validateOriginDestination(c, c))
    }

    // ── Waypoint list ────────────────────────────────────

    @Test
    fun `valid waypoint list`() {
        assertOk(
            RouteInputValidator.validateWaypointList(
                listOf(
                    RouteWaypoint("A", GeoCoordinate(10.0, 20.0)),
                    RouteWaypoint("B", GeoCoordinate(30.0, 40.0)),
                )
            )
        )
    }

    // EC-7
    @Test
    fun `single-element list is invalid`() {
        assertInvalid(
            "EC-7",
            RouteInputValidator.validateWaypointList(
                listOf(
                    RouteWaypoint("A", GeoCoordinate(10.0, 20.0)),
                )
            )
        )
    }

    @Test
    fun `empty list is invalid`() {
        assertInvalid("EC-7", RouteInputValidator.validateWaypointList(emptyList()))
    }

    // EC-8
    @Test
    fun `duplicate coordinates in list is invalid`() {
        val coord = GeoCoordinate(10.0, 20.0)
        assertInvalid(
            "EC-8",
            RouteInputValidator.validateWaypointList(
                listOf(
                    RouteWaypoint("A", coord),
                    RouteWaypoint("B", coord),
                )
            )
        )
    }

    @Test
    fun `list with invalid sub-waypoint propagates error`() {
        assertInvalid(
            "EC-5",
            RouteInputValidator.validateWaypointList(
                listOf(
                    RouteWaypoint("", GeoCoordinate(10.0, 20.0)),
                    RouteWaypoint("B", GeoCoordinate(30.0, 40.0)),
                )
            )
        )
    }

    // ── Helpers ──────────────────────────────────────────

    private fun assertOk(result: ValidationResult) {
        assertTrue("Expected Ok but got $result", result is ValidationResult.Ok)
    }

    private fun assertInvalid(expectedTag: String, result: ValidationResult) {
        assertTrue("Expected Invalid but got $result", result is ValidationResult.Invalid)
        val msg = (result as ValidationResult.Invalid).reason
        assertTrue(
            "Expected reason to contain '$expectedTag' but was: $msg",
            msg.contains(expectedTag)
        )
    }
}
