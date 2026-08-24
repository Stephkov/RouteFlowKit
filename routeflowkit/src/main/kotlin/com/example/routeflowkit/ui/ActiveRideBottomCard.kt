package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteProgress
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

/**
 * Reusable Active Ride Card for [com.example.routeflowkit.model.RouteFlowMode.ActiveRide].
 *
 * Handles:
 * - EC-9: Truncation/ellipsis for long text.
 * - EC-10: Fallback icon logic when vector asset is null.
 * - EC-11: Small-screen vertical scrolling.
 * - EC-12: RTL layout direction compatibility.
 */
@Composable
fun ActiveRideBottomCard(
    destination: RouteWaypoint,
    onSimulateArrival: () -> Unit,
    onCancelRide: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0.65f,
    etaMinutes: Int = 12,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icon: ImageVector? = RouteFlowDefaults.icons.activeRide,
    isMissingIconTest: Boolean = false,
    routeInfo: RouteInfo? = null,
    status: String? = null,
    routeProgress: RouteProgress? = null,
) {
    val displayedEtaMinutes = routeInfo?.durationSeconds?.div(60) ?: etaMinutes.toLong()
    val displayedEta = routeProgress?.remainingEta
        ?: strings.activeEtaFormat.format(displayedEtaMinutes)
    val displayedDistance = routeProgress?.remainingDistance
        ?: routeInfo?.let { strings.distanceFormat.format(it.distanceMeters / 1000.0) }
    val displayedStatus = routeProgress?.status ?: status ?: strings.activeRideStatus
    val displayedProgress = routeProgress?.clampedProgressFraction ?: progress
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(style.spacingMedium),
        shape = RoundedCornerShape(style.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = style.surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = style.cardElevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .verticalScroll(rememberScrollState()) // EC-11
                .padding(horizontal = style.spacingMedium, vertical = style.spacingSmall),
        ) {
            // Drag handle affordance
            CardHandleBar(color = style.handleColor)

            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!isMissingIconTest && icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = strings.activeRideIconDescription,
                        tint = style.primaryColor,
                        modifier = Modifier.size(28.dp),
                    )
                } else {
                    // Fallback for EC-10
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(style.primaryColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(strings.missingIconFallback, color = style.primaryColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.activeRideTitle,
                        color = style.onSurfaceColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${strings.destinationLabel}: ${destination.label}",
                        color = style.subtitleColor,
                        fontSize = 13.sp,
                        maxLines = 2, // EC-9
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(style.primaryColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = displayedEta,
                        color = style.primaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(style.spacingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = displayedStatus,
                    color = style.onSurfaceColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                displayedDistance?.let { distance ->
                    Text(
                        text = distance,
                        color = style.subtitleColor,
                        fontSize = 14.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(style.spacingMedium))

            // Linear Progress Indicator
            LinearProgressIndicator(
                progress = { displayedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = style.primaryColor,
                trackColor = style.backgroundColor,
            )

            Spacer(modifier = Modifier.height(style.spacingLarge))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onCancelRide,
                    modifier = Modifier
                        .weight(1f)
                        .height(style.buttonHeight),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(
                        text = strings.cancelButton,
                        color = style.errorColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Button(
                    onClick = onSimulateArrival,
                    modifier = Modifier
                        .weight(1f)
                        .height(style.buttonHeight),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = style.primaryColor),
                ) {
                    Text(
                        text = strings.simulateArrivalButton,
                        color = style.backgroundColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
