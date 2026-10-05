package com.duoc.ocularis.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Esquema de colores oscuros (opcional)
private val DarkColorScheme = darkColorScheme(
    primary = OcularisSecondary,
    secondary = OcularisAdicional,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// Esquema de colores claro (Principal de Ocularis)
private val LightColorScheme = lightColorScheme(
    primary = OcularisPrimary,
    secondary = OcularisSecondary,
    background = OcularisBackground,
    surface = OcularisBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = OcularisText,
    onSurface = OcularisText
)

@Composable
fun OcularisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Cambiamos dynamicColor a false para que Android no cambie los colores corporativos
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Se mantiene el de Type.kt
        content = content
    )
}