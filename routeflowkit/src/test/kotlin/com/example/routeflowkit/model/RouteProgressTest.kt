package com.example.routeflowkit.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RouteProgressTest {

    @Test
    fun `zero progress is accepted`() {
        assertEquals(0f, RouteProgress(progressFraction = 0f).clampedProgressFraction)
    }

    @Test
    fun `completed progress is accepted`() {
        assertEquals(1f, RouteProgress(progressFraction = 1f).clampedProgressFraction)
    }

    @Test
    fun `progress below zero is clamped`() {
        assertEquals(0f, RouteProgress(progressFraction = -0.5f).clampedProgressFraction)
    }

    @Test
    fun `progress above one is clamped`() {
        assertEquals(1f, RouteProgress(progressFraction = 1.5f).clampedProgressFraction)
    }

    @Test
    fun `non finite progress is safely treated as zero`() {
        assertEquals(0f, RouteProgress(progressFraction = Float.NaN).clampedProgressFraction)
        assertEquals(0f, RouteProgress(progressFraction = Float.POSITIVE_INFINITY).clampedProgressFraction)
    }

    @Test
    fun `current location is optional`() {
        assertNull(RouteProgress().currentLocation)
    }

    @Test
    fun `remaining presentation values are optional`() {
        val progress = RouteProgress()

        assertNull(progress.remainingEta)
        assertNull(progress.remainingDistance)
        assertNull(progress.status)
    }

    @Test
    fun `empty route has no split index`() {
        assertNull(routeProgressSplitIndex(pointCount = 0, progressFraction = 0.5f))
    }

    @Test
    fun `single point route splits safely`() {
        assertEquals(0, routeProgressSplitIndex(pointCount = 1, progressFraction = 0.75f))
    }

    @Test
    fun `split calculation stays inside route bounds`() {
        assertEquals(0, routeProgressSplitIndex(pointCount = 5, progressFraction = -10f))
        assertEquals(2, routeProgressSplitIndex(pointCount = 5, progressFraction = 0.5f))
        assertEquals(4, routeProgressSplitIndex(pointCount = 5, progressFraction = 10f))
    }
}
