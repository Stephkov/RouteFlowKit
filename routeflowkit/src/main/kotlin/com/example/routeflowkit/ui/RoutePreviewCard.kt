package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.routeflowkit.model.RouteInfo
import com.example.routeflowkit.model.RouteWaypoint
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

/**
 * Reusable Route Preview Card for [com.example.routeflowkit.model.RouteFlowMode.RoutePreview].
 *
 * Handles:
 * - EC-9: Truncation/ellipsis for long text.
 * - EC-10: Fallback icon logic when vector asset is null.
 * - EC-11: Small-screen vertical scrolling.
 * - EC-12: RTL layout direction compatibility.
 */
@Composable
fun RoutePreviewCard(
    origin: RouteWaypoint?,
    destination: RouteWaypoint,
    routeInfo: RouteInfo?,
    onConfirmRoute: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icon: ImageVector? = RouteFlowDefaults.icons.routePreview,
    isMissingIconTest: Boolean = false,
) {
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

            // Title Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!isMissingIconTest && icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = strings.routeIconDescription,
                        tint = style.primaryColor,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    // Fallback for EC-10
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(style.primaryColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                    ) {
                    Text(strings.missingIconFallback, color = style.primaryColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(style.spacingSmall))
                Text(
                    text = strings.routePreviewTitle,
                    color = style.onSurfaceColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // EC-9
                )
            }

            Spacer(modifier = Modifier.height(style.spacingMedium))

            // Origin / Destination Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(style.backgroundColor)
                    .padding(14.dp),
            ) {
                // Origin
                Text(
                    text = strings.originLabel,
                    color = style.subtitleColor,
                    fontSize = 11.sp,
                )
                Text(
                    text = origin?.label ?: strings.currentLocationLabel,
                    color = style.onSurfaceColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // EC-9
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Destination
                Text(
                    text = strings.destinationLabel,
                    color = style.subtitleColor,
                    fontSize = 11.sp,
                )
                Text(
                    text = destination.label,
                    color = style.onSurfaceColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2, // EC-9
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Stats summary if present
            if (routeInfo != null) {
                Spacer(modifier = Modifier.height(style.spacingMedium))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = style.primaryColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = strings.distanceFormat.format(routeInfo.distanceMeters / 1000.0),
                            color = style.primaryColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = strings.distanceLabel,
                            color = style.subtitleColor,
                            fontSize = 12.sp,
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = strings.estimatedTimeFormat.format(routeInfo.durationSeconds / 60),
                            color = style.primaryColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = strings.estimatedTimeLabel,
                            color = style.subtitleColor,
                            fontSize = 12.sp,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(style.spacingLarge))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .weight(1f)
                        .height(style.buttonHeight),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(
                        text = strings.backButton,
                        color = style.onSurfaceColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Button(
                    onClick = onConfirmRoute,
                    modifier = Modifier
                        .weight(1f)
                        .height(style.buttonHeight),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = style.primaryColor),
                ) {
                    Text(
                        text = strings.confirmRouteButton,
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
