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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.routeflowkit.state.RouteFlowUiState
import com.example.routeflowkit.style.LocalRouteFlowStyle
import com.example.routeflowkit.style.LocalRouteFlowStrings
import com.example.routeflowkit.style.RouteFlowDefaults
import com.example.routeflowkit.style.RouteFlowIcons
import com.example.routeflowkit.style.RouteFlowStyle
import com.example.routeflowkit.style.RouteFlowStrings

/**
 * Reusable Card displaying non-Ready UI states ([RouteFlowUiState]) or
 * validation errors (EC-1 through EC-12).
 *
 * Handles:
 * - EC-9: Truncation/ellipsis for long text.
 * - EC-10: Fallback icon logic when vector asset is null.
 * - EC-11: Small-screen vertical scrolling.
 * - EC-12: RTL layout direction compatibility.
 */
@Composable
fun RouteFlowMessageCard(
    uiState: RouteFlowUiState,
    onRetry: () -> Unit = {},
    onGrantPermission: () -> Unit = {},
    onEnableLocation: () -> Unit = {},
    modifier: Modifier = Modifier,
    edgeCaseCode: String? = null,
    style: RouteFlowStyle = LocalRouteFlowStyle.current,
    strings: RouteFlowStrings = LocalRouteFlowStrings.current,
    icons: RouteFlowIcons = RouteFlowDefaults.icons,
    isMissingIconTest: Boolean = false,
    showRecoveryAction: Boolean = true,
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

            when (uiState) {
                is RouteFlowUiState.Loading -> {
                    CircularProgressIndicator(
                        color = style.primaryColor,
                        modifier = Modifier.size(44.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = strings.loadingRouteMessage,
                        color = style.onSurfaceColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                is RouteFlowUiState.DestinationRequired -> {
                    MessageContent(
                        icon = icons.destinationRequired,
                        title = strings.selectDestinationTitle,
                        message = edgeCaseCode?.let { getEdgeCaseText(it, strings) }
                            ?: strings.destinationRequiredMessage,
                        buttonText = null,
                        onButtonClick = {},
                        style = style,
                        strings = strings,
                        isMissingIconTest = isMissingIconTest,
                    )
                }

                is RouteFlowUiState.RouteUnavailable -> {
                    MessageContent(
                        icon = icons.routeUnavailable,
                        title = strings.routeUnavailableTitle,
                        message = uiState.reason ?: strings.routeUnavailableMessage,
                        buttonText = strings.retryButton.takeIf { showRecoveryAction },
                        onButtonClick = onRetry,
                        style = style,
                        strings = strings,
                        isMissingIconTest = isMissingIconTest,
                    )
                }

                is RouteFlowUiState.LocationPermissionRequired -> {
                    MessageContent(
                        icon = icons.locationPermissionRequired,
                        title = strings.permissionRequiredTitle,
                        message = strings.permissionRequiredMessage,
                        buttonText = strings.grantPermissionButton.takeIf { showRecoveryAction },
                        onButtonClick = onGrantPermission,
                        style = style,
                        strings = strings,
                        isMissingIconTest = isMissingIconTest,
                    )
                }

                is RouteFlowUiState.LocationServicesDisabled -> {
                    MessageContent(
                        icon = icons.locationServicesDisabled,
                        title = strings.locationDisabledTitle,
                        message = strings.locationDisabledMessage,
                        buttonText = strings.enableLocationButton.takeIf { showRecoveryAction },
                        onButtonClick = onEnableLocation,
                        style = style,
                        strings = strings,
                        isMissingIconTest = isMissingIconTest,
                    )
                }

                is RouteFlowUiState.Error -> {
                    MessageContent(
                        icon = icons.error,
                        title = edgeCaseCode ?: strings.errorTitle,
                        message = edgeCaseCode?.let { getEdgeCaseText(it, strings) } ?: uiState.message,
                        buttonText = strings.retryButton.takeIf { showRecoveryAction },
                        onButtonClick = onRetry,
                        style = style,
                        strings = strings,
                        isMissingIconTest = isMissingIconTest,
                    )
                }

                else -> {
                    Text(
                        text = strings.readyLabel,
                        color = style.onSurfaceColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageContent(
    icon: ImageVector?,
    title: String,
    message: String,
    buttonText: String?,
    onButtonClick: () -> Unit,
    style: RouteFlowStyle,
    strings: RouteFlowStrings,
    isMissingIconTest: Boolean,
) {
    if (!isMissingIconTest && icon != null) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = style.primaryColor,
            modifier = Modifier.size(48.dp),
        )
    } else {
        // Fallback for EC-10
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(style.primaryColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(strings.messageIconFallback, color = style.primaryColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = title,
        color = titleColorForMessage(title, style),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = message,
        color = style.subtitleColor,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        maxLines = 4, // EC-9
        overflow = TextOverflow.Ellipsis,
    )

    if (buttonText != null) {
        Spacer(modifier = Modifier.height(style.spacingLarge))
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(style.buttonHeight),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = style.primaryColor),
        ) {
            Text(
                text = buttonText,
                color = style.backgroundColor,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun titleColorForMessage(title: String, style: RouteFlowStyle) =
    if (title.startsWith("EC-") || title.contains("Error")) style.errorColor else style.onSurfaceColor

private fun getEdgeCaseText(code: String, strings: RouteFlowStrings): String {
    return when (code) {
        "EC-1" -> strings.ec1LatitudeError
        "EC-2" -> strings.ec2LongitudeError
        "EC-3" -> strings.ec3NanError
        "EC-4" -> strings.ec4InfinityError
        "EC-5" -> strings.ec5BlankLabelError
        "EC-6" -> strings.ec6IdenticalPointsError
        "EC-7" -> strings.ec7InsufficientWaypointsError
        "EC-8" -> strings.ec8DuplicateWaypointsError
        "EC-9" -> strings.ec9LongTextLabel
        "EC-10" -> strings.ec10MissingIconLabel
        "EC-11" -> strings.ec11SmallScreenLabel
        "EC-12" -> strings.ec12RtlLabel
        else -> code
    }
}
