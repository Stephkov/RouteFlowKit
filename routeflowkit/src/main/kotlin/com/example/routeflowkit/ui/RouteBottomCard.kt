package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

/** Provider-neutral content for the reusable information card shown over the map. */
@Immutable
data class RouteBottomCardContent(
    val title: String,
    val subtitle: String,
    val eta: String? = null,
    val distance: String? = null,
    val secondaryInfo: String? = null,
    val primaryActionText: String,
    val secondaryActionText: String? = null,
    val icon: ImageVector? = null,
    val iconContentDescription: String? = null,
)

/**
 * A reusable map information card for ride, delivery, trail, and tracking experiences.
 * Text and actions are supplied by the host; the component only owns presentation.
 */
@Composable
fun RouteBottomCard(
    content: RouteBottomCardContent,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onSecondaryAction: (() -> Unit)? = null,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
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
                .heightIn(max = 390.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = style.spacingMedium, vertical = style.spacingSmall),
        ) {
            CardHandleBar(color = style.handleColor)
            Spacer(Modifier.height(style.spacingSmall))
            Row(verticalAlignment = Alignment.CenterVertically) {
                content.icon?.let {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(style.primaryColor.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = content.iconContentDescription,
                            tint = style.primaryColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = content.title,
                        color = style.onSurfaceColor,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = content.subtitle,
                        color = style.subtitleColor,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (content.eta != null || content.distance != null) {
                Spacer(Modifier.height(style.spacingMedium))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(style.backgroundColor.copy(alpha = 0.72f))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    content.eta?.let { Metric(it, strings.estimatedTimeLabel, style, Modifier.weight(1f)) }
                    content.distance?.let { Metric(it, strings.distanceLabel, style, Modifier.weight(1f)) }
                }
            }

            content.secondaryInfo?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = style.subtitleColor, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.height(style.spacingMedium))
            Button(
                onClick = onPrimaryAction,
                modifier = Modifier.fillMaxWidth().height(style.buttonHeight),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = style.primaryColor),
            ) {
                Text(content.primaryActionText, color = style.primaryButtonContentColor, fontWeight = FontWeight.Bold)
            }
            if (content.secondaryActionText != null && onSecondaryAction != null) {
                Spacer(Modifier.height(style.spacingSmall))
                OutlinedButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier.fillMaxWidth().height(style.buttonHeight),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = style.onSurfaceColor),
                ) { Text(content.secondaryActionText, fontWeight = FontWeight.SemiBold) }
            }
        }
    }
}

@Composable
private fun Metric(value: String, label: String, style: RouteFlowStyle, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = style.onSurfaceColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = style.subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}
