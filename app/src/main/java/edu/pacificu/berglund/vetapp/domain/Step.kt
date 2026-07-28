package edu.pacificu.berglund.vetapp.domain

import androidx.annotation.RawRes
import androidx.annotation.StringRes

/**
 * One step of the Safety S.O.P.
 *
 * Every step renders through the same screen — that was the original design's best
 * decision and it is preserved here. What changed is how a step declares what it *does*:
 *
 * The old model carried a `content` string that doubled as behaviour. The step screen
 * decided whether to show a breathing animation by comparing that display text
 * (`getContent().equalsIgnoreCase("follow the circle")`), which meant rewording a step
 * silently broke its animation and translating the app broke all of them. Behaviour now
 * hangs off [id] and [actions]; the display text is presentation only.
 */
data class Step(
    val id: StepId,
    @StringRes val title: Int,
    @StringRes val body: Int? = null,
    val actions: List<StepAction> = emptyList(),
)

/**
 * Stable identity for a step. Persisted in the veteran's saved order, so these names
 * must not change once shipped.
 *
 * The old code persisted *positional indices into a hardcoded list*, which meant
 * inserting or reordering the catalog would silently rewrite every existing veteran's
 * safety plan into a different set of steps.
 */
enum class StepId {
    DEEP_BREATH,
    REASONS_FOR_LIVING,
    HALT,
    TWO_MINUTE_MINDFUL_BREATHING,
    SAFETY_SOP,
    BODY_SCAN,
    LISTEN_TO_MUSIC,
    CALL_SOMEONE,
    CALL_HOTLINES,
    PUT_WEAPONS_DOWN,
    OBSERVE_AND_DESCRIBE,
    LEAVES_ON_A_STREAM,
    IDENTIFY_VALUES,
    MINDFUL_WALKING,
    PROGRESSIVE_MUSCLE_RELAXATION,
    DISTRACT_YOURSELF,
    AWARENESS_OF_THOUGHT,
    FOLLOW_THE_CIRCLE,
    URGE_SURFING,
    SOBER,
    FOUR_SQUARE_BREATHING,
    DIAPHRAGMATIC_BREATHING,
    FIVE_SENSES,
    MOUNTAIN_MEDITATION,
    DISTRACTIONS,
    WHOLE_BODY_BREATHING,
    BREATHING_SPACE,
    BREATHING_BODY,
}

/** What a step offers the veteran beyond its text. A step may have several. */
sealed interface StepAction {

    /**
     * Guided narration. The screen always pairs this with a repeat control — the
     * wireframe is explicit: "this needs a repeat button so they can do it over".
     */
    data class PlayAudio(@RawRes val res: Int) : StepAction

    /** An animated breathing pacer. */
    data class Breathing(val pattern: BreathingPattern) : StepAction

    /** Dial the veteran's configured emergency contact via the system dialler. */
    data object CallEmergencyContact : StepAction

    /** Hand off to the veteran's chosen music app, on an explicit tap. */
    data object OpenMusic : StepAction

    /** Offer the cognitive-distraction links. */
    data object Distractions : StepAction
}

/**
 * Breathing pacer timings, in seconds per phase.
 *
 * Taken from the skills master list: "Inhale is a 4 count, exhale is an 8 count" for the
 * circle, and for four-square "In, 2, 3, 4 … Hold, 2, 3, 4 … Out, 2, 3, 4 … Pause, 2, 3, 4".
 */
enum class BreathingPattern(
    val inhale: Int,
    val holdIn: Int,
    val exhale: Int,
    val holdOut: Int,
) {
    /** Expanding/contracting circle: 4 in, 8 out, no holds. */
    CIRCLE(inhale = 4, holdIn = 0, exhale = 8, holdOut = 0),

    /** Arrow tracing a square, 4 counts per side. */
    SQUARE(inhale = 4, holdIn = 4, exhale = 4, holdOut = 4),
    ;

    val totalSeconds: Int get() = inhale + holdIn + exhale + holdOut
}
