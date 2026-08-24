package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

/**
 * Reusable Destination Picker Card for [com.example.routeflowkit.model.RouteFlowMode.DestinationSelection].
 *
 * Handles:
 * - EC-9: Truncation/ellipsis for long text.
 * - EC-10: Fallback icon logic when vector asset is null.
 * - EC-11: Small-screen vertical scrolling.
 * - EC-12: RTL layout direction compatibility.
 */
@Composable
fun DestinationPickerCard(
    presetDestinations: List<RouteWaypoint>,
    onDestinationSelected: (RouteWaypoint) -> Unit,
    modifier: Modifier = Modifier,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    customIcon: ImageVector? = RouteFlowDefaults.icons.destinationPicker,
    searchIcon: ImageVector? = RouteFlowDefaults.icons.destinationSearch,
    destinationIcon: ImageVector? = RouteFlowDefaults.icons.destinationItem,
    isMissingIconTest: Boolean = false,
    selectedDestination: RouteWaypoint? = null,
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

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val iconToDraw = if (isMissingIconTest) null else customIcon
                if (iconToDraw != null) {
                    Icon(
                        imageVector = iconToDraw,
                        contentDescription = strings.destinationIconDescription,
                        tint = style.primaryColor,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(style.spacingSmall))
                } else {
                    // Fallback placeholder circle (EC-10)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(style.primaryColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = strings.missingIconFallback,
                            color = style.primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.width(style.spacingSmall))
                }

                Text(
                    text = strings.selectDestinationTitle,
                    color = style.onSurfaceColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // EC-9
                )
            }

            Spacer(modifier = Modifier.height(style.spacingMedium))

            // Search bar mockup
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = style.onSurfaceColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                    ),
                shape = RoundedCornerShape(12.dp),
                color = style.backgroundColor,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    searchIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = style.subtitleColor,
                        )
                        Spacer(modifier = Modifier.width(style.spacingSmall))
                    }
                    Text(
                        text = strings.destinationInputPlaceholder,
                        color = style.subtitleColor,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, // EC-9
                    )
                }
            }

            Spacer(modifier = Modifier.height(style.spacingMedium))

            // Preset destination items
            presetDestinations.forEach { waypoint ->
                val isSelected = waypoint == selectedDestination
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) style.primaryColor.copy(alpha = 0.10f)
                            else style.surfaceColor,
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) style.primaryColor.copy(alpha = 0.45f)
                            else style.onSurfaceColor.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { onDestinationSelected(waypoint) }
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    destinationIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = style.primaryColor,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = waypoint.label,
                            color = style.onSurfaceColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2, // EC-9
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "${waypoint.location.latitude}, ${waypoint.location.longitude}",
                            color = style.subtitleColor,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
