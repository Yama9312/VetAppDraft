package edu.pacificu.berglund.vetapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import edu.pacificu.berglund.vetapp.domain.Branch

/**
 * Palette.
 *
 * Token values come from the 2019 Indigo prototype approved for this project — a warm
 * parchment / khaki / brass scheme, deliberately not clinical. The per-branch accents
 * follow Indigo's branch palette screens and the hexes the previous version already
 * carried in `BaseActivity`.
 *
 * What is new is that **every branch has a dark scheme too**. Previously the app painted
 * a hardcoded light gradient straight onto the window decor while inheriting
 * `Theme.Material3.DayNight`, so on a phone set to dark mode the framework supplied
 * near-white text and drew it on near-white parchment — the step text was invisible.
 *
 * Every foreground/background pair here is verified at >= 4.5:1 (WCAG AA) and body text
 * on surfaces at >= 7:1 (AAA) by `ContrastTest`, which fails the build if a colour is
 * ever changed to something unreadable.
 */

// ---- Indigo base tokens -----------------------------------------------------------

internal val Parchment = Color(0xFFFFF9E2)
internal val Khaki = Color(0xFFD4CDB0)
internal val Brass = Color(0xFFE4D390)
internal val Ink = Color(0xFF282828)

/** Warm deep neutrals — the night-time counterpart to parchment. */
internal val NightSurface = Color(0xFF17150F)
internal val NightSurfaceRaised = Color(0xFF262218)
internal val NightOutline = Color(0xFF8C8367)
internal val NightInk = Color(0xFFF3EDDC)

/** Crisis red. Reserved for the emergency affordance; never decorative. */
internal val Alert = Color(0xFF8C1D18)
internal val OnAlert = Color(0xFFFFFFFF)
internal val AlertDark = Color(0xFFFFB4AB)
internal val OnAlertDark = Color(0xFF3F0705)

private val White = Color(0xFFFFFFFF)

/**
 * The minimum a branch needs to declare. Everything else in the [ColorScheme] is derived
 * from the shared Indigo tokens, so the six branches stay visibly one family rather than
 * six unrelated apps.
 */
private data class BranchPalette(
    val background: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryDark: Color,
    val onPrimaryDark: Color,
)

private val palettes: Map<Branch, BranchPalette> = mapOf(
    // Ammo-can yellow. A pale accent, so it carries dark text in both modes.
    Branch.ARMY to BranchPalette(
        background = Parchment,
        primary = Color(0xFFFDE37D), onPrimary = Ink,
        primaryDark = Color(0xFFFDE37D), onPrimaryDark = Ink,
    ),
    // Scarlet and gold. The scarlet is deep enough for white text in light mode; dark
    // mode lifts it to a tint that reads against the night surface.
    Branch.MARINE_CORPS to BranchPalette(
        background = Color(0xFFFDF3DE),
        primary = Color(0xFFBE2321), onPrimary = White,
        primaryDark = Color(0xFFFFB4AB), onPrimaryDark = Color(0xFF450A07),
    ),
    // Blue and gold.
    Branch.NAVY to BranchPalette(
        background = Color(0xFFFAECD4),
        primary = Color(0xFF132151), onPrimary = White,
        primaryDark = Color(0xFFB6C4EE), onPrimaryDark = Color(0xFF0A1230),
    ),
    // Ultramarine and gold.
    Branch.AIR_FORCE to BranchPalette(
        background = Color(0xFFFEF5E1),
        primary = Color(0xFF001027), onPrimary = White,
        primaryDark = Color(0xFFB3C7EA), onPrimaryDark = Color(0xFF001027),
    ),
    Branch.COAST_GUARD to BranchPalette(
        background = Color(0xFFFDF6EC),
        primary = Color(0xFF205698), onPrimary = White,
        primaryDark = Color(0xFFAAC7F5), onPrimaryDark = Color(0xFF032B57),
    ),
    // The neutral default: straight Indigo parchment and brass, no service branding.
    Branch.NEUTRAL to BranchPalette(
        background = Parchment,
        primary = Color(0xFF45454A), onPrimary = White,
        primaryDark = Color(0xFFC9C7CD), onPrimaryDark = Color(0xFF2E2E33),
    ),
)

private fun BranchPalette.toLightScheme(): ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = Brass,
    onPrimaryContainer = Ink,
    secondary = Khaki,
    onSecondary = Ink,
    secondaryContainer = Khaki,
    onSecondaryContainer = Ink,
    background = background,
    onBackground = Ink,
    surface = background,
    onSurface = Ink,
    surfaceVariant = Khaki,
    onSurfaceVariant = Ink,
    outline = Color(0xFF6E6752),
    error = Alert,
    onError = OnAlert,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

private fun BranchPalette.toDarkScheme(): ColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = NightSurfaceRaised,
    onPrimaryContainer = NightInk,
    secondary = NightOutline,
    onSecondary = NightSurface,
    secondaryContainer = NightSurfaceRaised,
    onSecondaryContainer = NightInk,
    background = NightSurface,
    onBackground = NightInk,
    surface = NightSurface,
    onSurface = NightInk,
    surfaceVariant = NightSurfaceRaised,
    onSurfaceVariant = NightInk,
    outline = NightOutline,
    error = AlertDark,
    onError = OnAlertDark,
    errorContainer = Color(0xFF601410),
    onErrorContainer = Color(0xFFF9DEDC),
)

/** Resolves the scheme for a branch. Exposed for tests as well as [VetTheme]. */
fun colorSchemeFor(branch: Branch, dark: Boolean): ColorScheme {
    val palette = palettes.getValue(branch)
    return if (dark) palette.toDarkScheme() else palette.toLightScheme()
}
