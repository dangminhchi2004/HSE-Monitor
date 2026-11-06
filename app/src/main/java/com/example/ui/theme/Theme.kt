package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val FieldOpsColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = CardSurface,
    primaryContainer = BrandPrimaryLight,
    onPrimaryContainer = BrandPrimaryDark,
    secondary = SemanticPass,
    onSecondary = CardSurface,
    secondaryContainer = SemanticPassLight,
    onSecondaryContainer = SemanticPassDark,
    tertiary = SemanticWarning,
    onTertiary = CardSurface,
    tertiaryContainer = SemanticWarningLight,
    onTertiaryContainer = SemanticWarning,
    error = SemanticFail,
    onError = CardSurface,
    errorContainer = SemanticFailLight,
    onErrorContainer = SemanticFailDark,
    background = BackgroundSurface,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = InputSurface,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

val FieldOpsShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FieldOpsColorScheme,
        typography = Typography,
        shapes = FieldOpsShapes,
        content = content
    )
}
