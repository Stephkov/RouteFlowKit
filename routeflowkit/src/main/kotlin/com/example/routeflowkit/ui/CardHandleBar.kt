package com.example.routeflowkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Visual handle bar indicator rendered at the top of bottom-sheet cards
 * to communicate draggable sheet affordance and visual hierarchy.
 */
@Composable
fun CardHandleBar(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.25f),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color),
        )
    }
}
