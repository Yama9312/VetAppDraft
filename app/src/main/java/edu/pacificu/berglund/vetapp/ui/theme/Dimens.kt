package edu.pacificu.berglund.vetapp.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Sizing rules for a crisis-state UI.
 *
 * These are not general Material defaults. The requirements are explicit that the app is
 * used by someone in acute distress, from a population skewing older and less
 * comfortable with phones: *"simplicity is crucial both for Veterans of older age where
 * technology is not a strong suit and in general to limit stressors when a crisis is
 * occurring"*.
 *
 * So the minimum touch target is 56dp rather than Material's 48dp, and primary actions
 * are larger still. Nothing here is gesture-only.
 */
object Dimens {
    /** Absolute floor for anything tappable. Above the 48dp Material minimum. */
    val MinTouchTarget = 56.dp

    /** Primary in-SOP navigation and the emergency affordance. */
    val PrimaryActionHeight = 64.dp

    val ScreenPadding = 20.dp
    val ContentSpacing = 16.dp
    val SectionSpacing = 28.dp

    val CardCorner = 16.dp
    val ButtonCorner = 12.dp

    /** Breathing pacer artwork. Sized as a fraction of width at the call site, capped. */
    val BreathingMaxSize = 280.dp
}
