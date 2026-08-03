package com.example.routeflowkit.model

import com.google.android.gms.maps.model.LatLng

/** Convert a public [GeoCoordinate] to an internal Google Maps [LatLng]. */
internal fun GeoCoordinate.toLatLng(): LatLng =
    LatLng(latitude, longitude)

/** Convert an internal Google Maps [LatLng] to a public [GeoCoordinate]. */
internal fun LatLng.toGeoCoordinate(): GeoCoordinate =
    GeoCoordinate(latitude, longitude)
