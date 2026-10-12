package com.example.mycalc.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MyCalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Эффект матового стекла (Liquid Glass)
fun Modifier.liquidGlass(
    shapeRadius: Dp = 24.dp,
    tintColor: Color = Color.White.copy(alpha = 0.12f),
    borderColor: Color = Color.White.copy(alpha = 0.22f)
): Modifier = this
    .clip(RoundedCornerShape(shapeRadius))
    .background(tintColor)
    .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
            listOf(borderColor, borderColor.copy(alpha = 0.05f))
        ),
        shape = RoundedCornerShape(shapeRadius)
    )
