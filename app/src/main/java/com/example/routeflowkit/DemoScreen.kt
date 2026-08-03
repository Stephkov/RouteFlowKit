package com.example.routeflowkit

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.ui.RouteFlowMap

/**
 * Demo screen that exercises every [RouteFlowUiState] variant
 * and renders [RouteFlowMap] when in the [RouteFlowUiState.Ready] state.
 */
@Composable
fun DemoScreen() {
    val states: List<Pair<String, RouteFlowUiState>> = remember {
        listOf(
            "Ready" to RouteFlowUiState.Ready(
                origin = RouteWaypoint(
                    "San Francisco",
                    GeoCoordinate(37.7749, -122.4194),
                ),
                destination = RouteWaypoint(
                    "Los Angeles",
                    GeoCoordinate(34.0522, -118.2437),
                ),
            ),
            "Loading" to RouteFlowUiState.Loading,
            "DestRequired" to RouteFlowUiState.DestinationRequired,
            "Unavailable" to RouteFlowUiState.RouteUnavailable(
                origin = GeoCoordinate(37.7749, -122.4194),
                destination = GeoCoordinate(34.0522, -118.2437),
                reason = "Demo — no real routing",
            ),
            "PermRequired" to RouteFlowUiState.LocationPermissionRequired,
            "LocDisabled" to RouteFlowUiState.LocationServicesDisabled,
            "Error" to RouteFlowUiState.Error("Demo error"),
        )
    }

    var selectedIndex by remember { mutableStateOf(0) }
    val uiState = states[selectedIndex].second

    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            // Map takes available space
            Box(Modifier.weight(1f)) {
                if (uiState is RouteFlowUiState.Ready) {
                    RouteFlowMap(
                        origin = uiState.origin,
                        destination = uiState.destination,
                    )
                } else {
                    // Placeholder for non-Ready states
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(uiState::class.simpleName ?: "Unknown")
                    }
                }
            }

            // State selector chips
            Row(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                states.forEachIndexed { index, (label, _) ->
                    FilterChip(
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
        }
    }
}
