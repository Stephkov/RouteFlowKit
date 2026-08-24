package com.example.routeflowkit

import com.example.routeflowkit.model.RouteFlowMode
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteFlowData
import com.example.routeflowkit.style.RouteFlowCardPosition
import com.example.routeflowkit.style.RouteFlowLayout
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteFlowModeTest {

    @Test
    fun `verify route flow modes exist`() {
        val modes = RouteFlowMode.entries
        assertEquals(4, modes.size)
        assertEquals(RouteFlowMode.DestinationSelection, modes[0])
        assertEquals(RouteFlowMode.RoutePreview, modes[1])
        assertEquals(RouteFlowMode.ActiveRide, modes[2])
        assertEquals(RouteFlowMode.Arrived, modes[3])
    }

    @Test
    fun `verify style defaults and presets`() {
        val darkStyle = RouteFlowStyle.TechSlateDark
        val lightStyle = RouteFlowStyle.EmeraldCleanLight

        assertNotNull(darkStyle.primaryColor)
        assertNotNull(lightStyle.primaryColor)
        assertEquals(16, darkStyle.cardCornerRadius.value.toInt())
        assertEquals(24, lightStyle.cardCornerRadius.value.toInt())
        assertEquals(48, darkStyle.buttonHeight.value.toInt())
        assertEquals(8, darkStyle.spacingSmall.value.toInt())
        assertEquals(16, darkStyle.spacingMedium.value.toInt())
        assertEquals(24, darkStyle.spacingLarge.value.toInt())
    }

    @Test
    fun `verify default strings and edge case identifiers`() {
        val strings = RouteFlowStrings()
        assertEquals("Where to?", strings.selectDestinationTitle)
        assertEquals("Confirm Route", strings.confirmRouteButton)
        assertEquals("You've Arrived!", strings.arrivedTitle)
        assertTrue(strings.ec1LatitudeError.contains("EC-1"))
        assertTrue(strings.ec9LongTextLabel.contains("EC-9"))
        assertTrue(strings.ec10MissingIconLabel.contains("EC-10"))
        assertTrue(strings.ec11SmallScreenLabel.contains("EC-11"))
        assertTrue(strings.ec12RtlLabel.contains("EC-12"))
    }

    @Test
    fun `verify Hebrew localization preset`() {
        val hebrewStrings = RouteFlowStrings.Hebrew
        assertEquals("לאן נוסעים?", hebrewStrings.selectDestinationTitle)
        assertEquals("אישור מסלול", hebrewStrings.confirmRouteButton)
        assertEquals("הגעת ליעד!", hebrewStrings.arrivedTitle)
        assertTrue(hebrewStrings.ec12RtlLabel.contains("EC-12"))
    }

    @Test
    fun `route flow data accepts optional host supplied current location`() {
        val currentLocation = GeoCoordinate(32.0853, 34.7818)

        assertEquals(currentLocation, RouteFlowData(currentLocation = currentLocation).currentLocation)
        assertEquals(null, RouteFlowData().currentLocation)
    }

    @Test
    fun `layout defaults to bottom with no additional padding`() {
        val layout = RouteFlowLayout()

        assertEquals(RouteFlowCardPosition.Bottom, layout.cardPosition)
        assertEquals(0, layout.cardOuterPadding.value.toInt())
    }

    @Test
    fun `active ride data and default strings expose all metrics`() {
        val strings = RouteFlowStrings()
        val data = RouteFlowData(status = "Driver nearby")

        assertEquals("Driver nearby", data.status)
        assertEquals("On the way", strings.activeRideStatus)
        assertEquals("12 min", strings.activeEtaFormat.format(12))
        assertEquals("8.4 km", strings.distanceFormat.format(8.4))
    }

    @Test
    fun `map marker customization defaults preserve fallback markers`() {
        val icons = RouteFlowIcons()

        assertEquals(null, icons.startMarkerResourceId)
        assertEquals(null, icons.destinationMarkerResourceId)
        assertEquals(null, icons.currentLocationMarkerResourceId)
    }
}
