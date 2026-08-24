package com.example.routeflowkit.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutePolylineMapperTest {

    @Test
    fun `valid route points convert in order`() {
        val points = listOf(
            GeoCoordinate(32.0853, 34.7818),
            GeoCoordinate(32.1093, 34.8555),
        )

        val mapped = points.toValidLatLngPolyline()

        assertEquals(2, mapped.size)
        assertEquals(points.first().latitude, mapped.first().latitude, 0.0)
        assertEquals(points.last().longitude, mapped.last().longitude, 0.0)
    }

    @Test
    fun `empty and insufficient routes do not render`() {
        assertTrue(emptyList<GeoCoordinate>().toValidLatLngPolyline().isEmpty())
        assertTrue(listOf(GeoCoordinate(0.0, 0.0)).toValidLatLngPolyline().isEmpty())
    }

    @Test
    fun `route containing invalid coordinate does not render`() {
        val points = listOf(
            GeoCoordinate(0.0, 0.0),
            GeoCoordinate(Double.NaN, 10.0),
        )

        assertTrue(points.toValidLatLngPolyline().isEmpty())
    }
}
