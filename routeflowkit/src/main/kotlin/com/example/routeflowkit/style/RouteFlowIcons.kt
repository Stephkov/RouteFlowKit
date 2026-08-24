package com.example.routeflowkit.style

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Icons displayed by RouteFlowKit. Marker resource IDs refer to host Android drawable
 * resources and are converted to Google Maps descriptors internally. Null uses the existing
 * default marker appearance.
 */
@Immutable
data class RouteFlowIcons(
    val destinationPicker: ImageVector? = Icons.Default.Place,
    val destinationSearch: ImageVector? = Icons.Default.Search,
    val destinationItem: ImageVector? = Icons.Default.LocationOn,
    val routePreview: ImageVector? = Icons.Default.Place,
    val activeRide: ImageVector? = Icons.Default.Place,
    val arrived: ImageVector? = Icons.Default.CheckCircle,
    val destinationRequired: ImageVector? = Icons.Default.Info,
    val routeUnavailable: ImageVector? = Icons.Default.Warning,
    val locationPermissionRequired: ImageVector? = Icons.Default.Lock,
    val locationServicesDisabled: ImageVector? = Icons.Default.Lock,
    val error: ImageVector? = Icons.Default.Warning,
    val startMarkerResourceId: Int? = null,
    val destinationMarkerResourceId: Int? = null,
    val currentLocationMarkerResourceId: Int? = null,
)

/** Centralized defaults shared by RouteFlowKit's public components. */
object RouteFlowDefaults {
    val icons: RouteFlowIcons = RouteFlowIcons()
}
