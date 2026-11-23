package com.duoc.ocularis.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

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

// Esquema de colores de Alto Contraste para Accesibilidad
private val HighContrastColorScheme = darkColorScheme(
    primary = Color(0xFFFFFF00), // Amarillo de alto contraste
    secondary = Color(0xFF00FFFF), // Cyan de alto contraste
    background = Color.Black,
    surface = Color(0xFF121212),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun OcularisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    textScale: Float = 1.0f,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast -> HighContrastColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val scaledTypography = Typography(
        bodyLarge = Typography.bodyLarge.copy(
            fontSize = (16 * textScale).sp,
            lineHeight = (24 * textScale).sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}