package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * Reusable Arrival Card for [com.example.routeflowkit.model.RouteFlowMode.Arrived].
 *
 * Handles:
 * - EC-9: Truncation/ellipsis for long text.
 * - EC-10: Fallback icon logic when vector asset is null.
 * - EC-11: Small-screen vertical scrolling.
 * - EC-12: RTL layout direction compatibility.
 */
@Composable
fun ArrivedCard(
    destination: RouteWaypoint?,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icon: ImageVector? = RouteFlowDefaults.icons.arrived,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Drag handle affordance
            CardHandleBar(color = style.handleColor)

            Spacer(modifier = Modifier.height(8.dp))

            // Icon badge
            if (!isMissingIconTest && icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = strings.arrivedIconDescription,
                    tint = style.successColor,
                    modifier = Modifier.size(56.dp),
                )
            } else {
                // Fallback for EC-10
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(style.successColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(strings.arrivedIconFallback, color = style.successColor, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = strings.arrivedTitle,
                color = style.onSurfaceColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = strings.arrivedSubtitle,
                color = style.subtitleColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (destination != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(style.backgroundColor)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
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
                        textAlign = TextAlign.Center,
                        maxLines = 2, // EC-9
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.height(style.spacingLarge))

            Button(
                onClick = onDoneClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(style.buttonHeight),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = style.primaryColor),
            ) {
                Text(
                    text = strings.doneButton,
                    color = style.backgroundColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
