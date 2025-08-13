package com.example.saferoute.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.darkColorScheme

private val colorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = DeepPurple,
    background = DarkBlue,
    onPrimary = White,
)

@Composable
fun SafeRouteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
