package com.unifytv.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core palette — a deep indigo night with electric-violet and teal accents.
val Ink = Color(0xFF0B0712)
val Midnight = Color(0xFF120B2E)
val Plum = Color(0xFF2A0E4F)
val Violet = Color(0xFF7C5CFF)
val VioletSoft = Color(0xFFB3A2FF)
val Teal = Color(0xFF35E0C0)
val Coral = Color(0xFFFF6B8B)
val Cloud = Color(0xFFF4F1FF)
val Mist = Color(0xFFB8B2D6)
val Glass = Color(0x14FFFFFF)
val GlassStrong = Color(0x29FFFFFF)

/** The ambient background gradient used behind every screen. */
val AppBackground = Brush.linearGradient(
    colors = listOf(Ink, Midnight, Plum),
)

/** Accent gradient for focused elements and the brand mark. */
val AccentGradient = Brush.linearGradient(
    colors = listOf(Violet, Teal),
)

/** Scrim placed under poster art so overlaid text stays legible. */
val PosterScrim = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color(0xCC0B0712)),
)
