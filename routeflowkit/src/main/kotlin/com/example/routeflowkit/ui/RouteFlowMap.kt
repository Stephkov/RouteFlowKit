package com.example.routeflowkit.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.routeflowkit.model.GeoCoordinate
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.model.routeProgressSplitIndex
import com.example.routeflowkit.model.toLatLng
import com.example.routeflowkit.model.toValidLatLngPolyline
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowMapPreset
import com.example.routeflowkit.style.RouteFlowStrings
import com.example.routeflowkit.style.RouteFlowStyle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/** Google Maps-backed renderer. All consumer-facing inputs remain provider-neutral. */
@Composable
fun RouteFlowMap(
    origin: RouteWaypoint? = null,
    destination: RouteWaypoint? = null,
    currentLocation: GeoCoordinate? = null,
    modifier: Modifier = Modifier,
    routeInfo: RouteInfo? = null,
    style: RouteFlowStyle = RouteFlowStyle(),
    strings: RouteFlowStrings = RouteFlowStrings(),
    icons: RouteFlowIcons = RouteFlowDefaults.icons,
    progressFraction: Float? = null,
    mapPreset: RouteFlowMapPreset = RouteFlowMapPreset.Default,
) {
    val context = LocalContext.current
    val cameraState = rememberCameraPositionState()
    val originMarkerState = rememberMarkerState()
    val destinationMarkerState = rememberMarkerState()
    val currentLocationMarkerState = rememberMarkerState()
    val routePoints = routeInfo?.polylinePoints.orEmpty().toValidLatLngPolyline()
    val framingPoints = remember(routePoints, origin?.location, destination?.location) {
        buildList {
            origin?.location?.toLatLng()?.let(::add)
            addAll(routePoints)
            destination?.location?.toLatLng()?.let(::add)
        }.distinct()
    }
    var mapLoaded by remember { mutableStateOf(false) }
    val mapStyle = remember(mapPreset) {
        when (mapPreset) {
            RouteFlowMapPreset.Default -> null
            RouteFlowMapPreset.Clean -> MapStyleOptions(CLEAN_MAP_STYLE_JSON)
            RouteFlowMapPreset.Minimal -> MapStyleOptions(MINIMAL_MAP_STYLE_JSON)
        }
    }
    fun cameraUpdate() = when {
        framingPoints.size >= 2 -> CameraUpdateFactory.newLatLngBounds(
            LatLngBounds.builder().apply { framingPoints.forEach(::include) }.build(),
            96,
        )
        framingPoints.size == 1 -> CameraUpdateFactory.newLatLngZoom(framingPoints.first(), 15f)
        currentLocation != null -> CameraUpdateFactory.newLatLngZoom(currentLocation.toLatLng(), 15f)
        else -> null
    }

    LaunchedEffect(mapLoaded, framingPoints) {
        if (mapLoaded) cameraUpdate()?.let { runCatching { cameraState.animate(it, 700) } }
    }
    LaunchedEffect(origin?.location) {
        origin?.location?.let { originMarkerState.position = it.toLatLng() }
    }
    LaunchedEffect(destination?.location) {
        destination?.location?.let { destinationMarkerState.position = it.toLatLng() }
    }
    LaunchedEffect(currentLocation) {
        currentLocation?.let { currentLocationMarkerState.position = it.toLatLng() }
    }

    Box(modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            contentPadding = PaddingValues(
                start = 24.dp,
                top = 96.dp,
                end = 24.dp,
                bottom = 280.dp,
            ),
            properties = MapProperties(
                isMyLocationEnabled = false,
                mapStyleOptions = mapStyle,
            ),
            uiSettings = MapUiSettings(
                compassEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
            ),
            onMapLoaded = { mapLoaded = true },
        ) {
            if (routePoints.isNotEmpty()) {
                Polyline(points = routePoints, color = Color.White.copy(alpha = 0.9f), width = style.routeWidth + 7f, zIndex = 1f)
                if (progressFraction == null) {
                    Polyline(points = routePoints, color = style.routeColor, width = style.routeWidth, zIndex = 2f)
                } else {
                    val splitIndex = routeProgressSplitIndex(routePoints.size, progressFraction) ?: 0
                    val completedPoints = routePoints.take(splitIndex + 1)
                    val remainingPoints = routePoints.drop(splitIndex)
                    if (completedPoints.size >= 2) {
                        Polyline(
                            points = completedPoints,
                            color = style.completedRouteColor,
                            width = style.completedRouteWidth,
                            zIndex = 3f,
                        )
                    }
                    if (remainingPoints.size >= 2) {
                        Polyline(
                            points = remainingPoints,
                            color = style.remainingRouteColor,
                            width = style.routeWidth,
                            zIndex = 2f,
                        )
                    }
                }
            }
            origin?.let {
                Marker(
                    state = originMarkerState,
                    title = it.label,
                    icon = markerDescriptor(context, icons.startMarkerResourceId, BitmapDescriptorFactory.HUE_GREEN),
                )
            }
            destination?.let {
                Marker(
                    state = destinationMarkerState,
                    title = it.label,
                    icon = markerDescriptor(context, icons.destinationMarkerResourceId, BitmapDescriptorFactory.HUE_RED),
                )
            }
            currentLocation?.let {
                Marker(
                    state = currentLocationMarkerState,
                    title = strings.currentLocationLabel,
                    icon = markerDescriptor(context, icons.currentLocationMarkerResourceId, BitmapDescriptorFactory.HUE_AZURE),
                    zIndex = 3f,
                )
            }
        }

        FloatingActionButton(
            onClick = {
                val update = currentLocation
                    ?.let { CameraUpdateFactory.newLatLngZoom(it.toLatLng(), 15f) }
                    ?: cameraUpdate()
                update?.let { cameraState.move(it) }
            },
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp, bottom = 80.dp).size(48.dp),
            shape = CircleShape,
            containerColor = style.mapControlColor,
            contentColor = style.primaryColor,
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = strings.currentLocationLabel, modifier = Modifier.size(22.dp))
        }
    }
}

private fun markerDescriptor(
    context: Context,
    resourceId: Int?,
    fallbackHue: Float,
): BitmapDescriptor = resourceId?.let { id ->
    runCatching {
        val drawable = requireNotNull(ContextCompat.getDrawable(context, id))
        val width = drawable.intrinsicWidth.coerceAtLeast(1)
        val height = drawable.intrinsicHeight.coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }.getOrNull()
} ?: BitmapDescriptorFactory.defaultMarker(fallbackHue)

/** Internal Google Maps styling; the public API exposes only [RouteFlowMapPreset]. */
private const val CLEAN_MAP_STYLE_JSON = """
[
  {"elementType":"geometry","stylers":[{"color":"#f4f6f5"}]},
  {"elementType":"labels.icon","stylers":[{"visibility":"off"}]},
  {"elementType":"labels.text.fill","stylers":[{"color":"#7b8581"}]},
  {"elementType":"labels.text.stroke","stylers":[{"color":"#f4f6f5"}]},
  {"featureType":"administrative","elementType":"geometry.stroke","stylers":[{"color":"#dfe4e2"}]},
  {"featureType":"landscape","elementType":"geometry","stylers":[{"color":"#f4f6f5"}]},
  {"featureType":"poi","stylers":[{"visibility":"off"}]},
  {"featureType":"road","elementType":"geometry","stylers":[{"color":"#ffffff"}]},
  {"featureType":"road","elementType":"geometry.stroke","stylers":[{"color":"#e8ecea"}]},
  {"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#909a96"}]},
  {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#e9eeec"}]},
  {"featureType":"transit","stylers":[{"visibility":"off"}]},
  {"featureType":"water","elementType":"geometry","stylers":[{"color":"#dcebea"}]},
  {"featureType":"water","elementType":"labels.text.fill","stylers":[{"color":"#879b98"}]}
]
"""

private const val MINIMAL_MAP_STYLE_JSON = """
[
  {"elementType":"geometry","stylers":[{"color":"#f7f8f7"}]},
  {"elementType":"labels","stylers":[{"visibility":"off"}]},
  {"featureType":"administrative","elementType":"geometry","stylers":[{"visibility":"off"}]},
  {"featureType":"landscape","elementType":"geometry","stylers":[{"color":"#f7f8f7"}]},
  {"featureType":"poi","stylers":[{"visibility":"off"}]},
  {"featureType":"road","elementType":"geometry","stylers":[{"color":"#ffffff"}]},
  {"featureType":"road","elementType":"geometry.stroke","stylers":[{"color":"#e8ecea"},{"weight":0.8}]},
  {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#edf0ef"}]},
  {"featureType":"transit","stylers":[{"visibility":"off"}]},
  {"featureType":"water","elementType":"geometry","stylers":[{"color":"#e2efee"}]}
]
"""
