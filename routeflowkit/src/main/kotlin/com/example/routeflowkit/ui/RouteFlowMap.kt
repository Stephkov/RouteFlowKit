package com.example.routeflowkit.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.model.toLatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

/**
 * A thin Composable that renders a [GoogleMap] with optional
 * origin / destination markers.
 *
 * This is intentionally minimal for Week 2: no polyline overlay,
 * no camera animation, no route calculation.
 */
@Composable
fun RouteFlowMap(
    origin: RouteWaypoint? = null,
    destination: RouteWaypoint? = null,
    modifier: Modifier = Modifier,
) {
    GoogleMap(modifier = modifier.fillMaxSize()) {
        origin?.let {
            Marker(
                state = rememberMarkerState(
                    position = it.location.toLatLng()
                ),
                title = it.label,
            )
        }
        destination?.let {
            Marker(
                state = rememberMarkerState(
                    position = it.location.toLatLng()
                ),
                title = it.label,
            )
        }
    }
}
