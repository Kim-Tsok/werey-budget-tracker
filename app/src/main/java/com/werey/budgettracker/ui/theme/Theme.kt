package com.werey.budgettracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val WereyScheme = darkColorScheme(
    primary = Plum,
    onPrimary = PlumInk,
    secondary = Plum,
    background = Night,
    surface = NightSurface,
    onBackground = LightText,
    onSurface = LightText
)

@Composable
fun WereyBudgetTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = WereyScheme, typography = Typography, content = content)
}
