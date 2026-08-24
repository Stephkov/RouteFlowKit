package com.example.routeflowkit.model

import com.google.android.gms.maps.model.LatLng

/** Convert a public [GeoCoordinate] to an internal Google Maps [LatLng]. */
internal fun GeoCoordinate.toLatLng(): LatLng =
    LatLng(latitude, longitude)

/** Convert an internal Google Maps [LatLng] to a public [GeoCoordinate]. */
internal fun LatLng.toGeoCoordinate(): GeoCoordinate =
    GeoCoordinate(latitude, longitude)

/** Safely maps a renderable provider-independent route to Google Maps coordinates. */
internal fun List<GeoCoordinate>.toValidLatLngPolyline(): List<LatLng> =
    takeIf { points ->
        points.size >= 2 && points.all { point ->
            point.latitude.isFinite() &&
                point.longitude.isFinite() &&
                point.latitude in -90.0..90.0 &&
                point.longitude in -180.0..180.0
        }
    }?.map(GeoCoordinate::toLatLng).orEmpty()
