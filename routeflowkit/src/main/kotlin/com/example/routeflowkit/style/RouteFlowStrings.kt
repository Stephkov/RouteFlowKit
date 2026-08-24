package com.example.routeflowkit.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Strings and localized messages used by RouteFlowKit UI components.
 */
@Immutable
data class RouteFlowStrings(
    val selectDestinationTitle: String = "Where to?",
    val destinationInputPlaceholder: String = "Search destination...",
    val originLabel: String = "Pickup Location",
    val destinationLabel: String = "Destination",
    val routePreviewTitle: String = "Route Preview",
    val confirmRouteButton: String = "Confirm Route",
    val startRideButton: String = "Start Ride",
    val backButton: String = "Back",
    val cancelButton: String = "Cancel",
    val activeRideTitle: String = "Ride in Progress",
    val simulateArrivalButton: String = "Simulate Arrival",
    val arrivedTitle: String = "You've Arrived!",
    val arrivedSubtitle: String = "Hope you enjoyed your ride.",
    val doneButton: String = "Done",
    val retryButton: String = "Retry",
    val grantPermissionButton: String = "Grant Permission",
    val enableLocationButton: String = "Enable Location",
    val loadingRouteMessage: String = "Calculating Route...",
    val destinationRequiredMessage: String = "Please choose a destination to proceed.",
    val routeUnavailableTitle: String = "Route Unavailable",
    val routeUnavailableMessage: String = "No route found between the specified origin and destination.",
    val routeOriginRequiredMessage: String = "A route origin is required.",
    val renderableRouteRequiredMessage: String = "A renderable route was not supplied.",
    val permissionRequiredTitle: String = "Permission Required",
    val permissionRequiredMessage: String = "RouteFlowKit needs location access to determine your origin and calculate routes.",
    val locationDisabledTitle: String = "Location Disabled",
    val locationDisabledMessage: String = "GPS or location services are turned off on your device. Please enable location.",
    val errorTitle: String = "Error Occurred",
    val readyLabel: String = "Ready",
    val currentLocationLabel: String = "Current Location",
    val distanceLabel: String = "Distance",
    val estimatedTimeLabel: String = "Est. Time",
    val activeRideStatus: String = "On the way",
    val distanceFormat: String = "%.1f km",
    val estimatedTimeFormat: String = "%d mins",
    val activeEtaFormat: String = "%d min",
    val destinationIconDescription: String = "Destination Icon",
    val routeIconDescription: String = "Navigation Icon",
    val activeRideIconDescription: String = "Active Ride Icon",
    val arrivedIconDescription: String = "Success Icon",
    val missingIconFallback: String = "?",
    val messageIconFallback: String = "!",
    val arrivedIconFallback: String = "✓",
    // Edge case text descriptions (EC-1 through EC-12)
    val ec1LatitudeError: String = "[EC-1] Invalid Latitude: Must be between -90.0 and 90.0",
    val ec2LongitudeError: String = "[EC-2] Invalid Longitude: Must be between -180.0 and 180.0",
    val ec3NanError: String = "[EC-3] Invalid Coordinate: Value cannot be NaN",
    val ec4InfinityError: String = "[EC-4] Invalid Coordinate: Value cannot be Infinity",
    val ec5BlankLabelError: String = "[EC-5] Invalid Waypoint: Label must not be blank",
    val ec6IdenticalPointsError: String = "[EC-6] Invalid Route: Origin and Destination cannot be identical",
    val ec7InsufficientWaypointsError: String = "[EC-7] Invalid Route: Requires at least 2 waypoints",
    val ec8DuplicateWaypointsError: String = "[EC-8] Invalid Route: Duplicate coordinates detected",
    val ec9LongTextLabel: String = "[EC-9] Long Text: Highly verbose label testing text truncation and wrap behavior",
    val ec10MissingIconLabel: String = "[EC-10] Missing Icon: Gracefully handling null or unavailable vector asset",
    val ec11SmallScreenLabel: String = "[EC-11] Small Screen: Card layout constrained to tight height/width bounds",
    val ec12RtlLabel: String = "[EC-12] RTL Layout: Displaying right-to-left layout and localized text",
) {
    companion object {
        /** Built-in Hebrew localization preset for testing authentic RTL presentation (EC-12). */
        val Hebrew = RouteFlowStrings(
            selectDestinationTitle = "לאן נוסעים?",
            destinationInputPlaceholder = "חפש יעד...",
            originLabel = "נקודת איסוף",
            destinationLabel = "יעד נסיעה",
            routePreviewTitle = "תצוגת מסלול",
            confirmRouteButton = "אישור מסלול",
            startRideButton = "התחל נסיעה",
            backButton = "חזרה",
            cancelButton = "ביטול",
            activeRideTitle = "נסיעה פעילה",
            simulateArrivalButton = "סימולציית הגעה",
            arrivedTitle = "הגעת ליעד!",
            arrivedSubtitle = "מקווים שנהנית מהנסיעה.",
            doneButton = "סיום",
            retryButton = "נסה שוב",
            grantPermissionButton = "אישור הרשאה",
            enableLocationButton = "הפעל מיקום",
            loadingRouteMessage = "מחשב מסלול...",
            destinationRequiredMessage = "יש לבחור יעד כדי להמשיך.",
            routeUnavailableTitle = "המסלול אינו זמין",
            routeUnavailableMessage = "לא נמצא מסלול בין נקודת המוצא ליעד.",
            permissionRequiredTitle = "נדרשת הרשאה",
            permissionRequiredMessage = "נדרשת גישה למיקום כדי להציג את נקודת המוצא.",
            locationDisabledTitle = "המיקום מושבת",
            locationDisabledMessage = "שירותי המיקום במכשיר כבויים. יש להפעיל מיקום.",
            errorTitle = "אירעה שגיאה",
            readyLabel = "מוכן",
            currentLocationLabel = "מיקום נוכחי",
            distanceLabel = "מרחק",
            estimatedTimeLabel = "זמן משוער",
            activeRideStatus = "בדרך",
            ec12RtlLabel = "[EC-12] תצוגת מימין לשמאל: תמיכה מלאה בשפה העברית ובכיוון RTL",
        )
    }
}

/** Composition-local RouteFlowKit copy used by independently composed UI components. */
val LocalRouteFlowStrings = staticCompositionLocalOf { RouteFlowStrings() }
