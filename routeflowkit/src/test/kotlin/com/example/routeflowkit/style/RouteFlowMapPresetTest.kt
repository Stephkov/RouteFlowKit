package com.example.routeflowkit.style

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteFlowMapPresetTest {

    @Test
    fun `default clean and minimal map presentations remain available`() {
        assertEquals(RouteFlowMapPreset.Default, RouteFlowMapPreset.valueOf("Default"))
        assertTrue(RouteFlowMapPreset.entries.contains(RouteFlowMapPreset.Clean))
        assertTrue(RouteFlowMapPreset.entries.contains(RouteFlowMapPreset.Minimal))
    }
}
