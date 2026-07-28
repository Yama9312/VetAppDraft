package edu.pacificu.berglund.vetapp.data

import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.BreathingPattern
import edu.pacificu.berglund.vetapp.domain.Step
import edu.pacificu.berglund.vetapp.domain.StepAction
import edu.pacificu.berglund.vetapp.domain.StepId

/**
 * The catalog of steps a veteran and their clinician can choose from.
 *
 * The requirements put it as "28 possible steps — veteran chooses 8 with practitian".
 * This is that library; the veteran's chosen subset and order lives in the database.
 *
 * Previously these 28 steps were built inline in `MainActivity.onCreate` on every launch,
 * with their display labels duplicated *again* as a parallel 28-string list in
 * `ChangeStepOrderFragment` — the two had to be edited in lockstep or the reorder screen
 * would relabel the wrong steps. There is now one definition.
 *
 * All instructional text is clinical content authored by the project's psychologist and
 * is carried over verbatim; it lives in `strings.xml` so it can be translated (the docs
 * call for English and Spanish).
 */
object StepCatalog {

    val steps: List<Step> = listOf(
        Step(
            id = StepId.DEEP_BREATH,
            title = R.string.step_deep_breath,
        ),
        Step(
            id = StepId.REASONS_FOR_LIVING,
            title = R.string.step_reasons_for_living,
            body = R.string.step_reasons_for_living_body,
            actions = listOf(StepAction.CallEmergencyContact),
        ),
        Step(
            id = StepId.HALT,
            title = R.string.step_halt,
            body = R.string.step_halt_body,
        ),
        Step(
            id = StepId.TWO_MINUTE_MINDFUL_BREATHING,
            title = R.string.step_two_minute_mindful_breathing,
            actions = listOf(StepAction.PlayAudio(R.raw.two_minutes_of_mindful_breathing_vf)),
        ),
        Step(
            id = StepId.SAFETY_SOP,
            title = R.string.step_safety_sop,
        ),
        Step(
            id = StepId.BODY_SCAN,
            title = R.string.step_body_scan,
            actions = listOf(StepAction.PlayAudio(R.raw.body_scan_v2)),
        ),
        Step(
            id = StepId.LISTEN_TO_MUSIC,
            title = R.string.step_listen_to_music,
            actions = listOf(StepAction.OpenMusic),
        ),
        Step(
            id = StepId.CALL_SOMEONE,
            title = R.string.step_call_someone,
            body = R.string.step_call_someone_body,
            actions = listOf(StepAction.CallEmergencyContact),
        ),
        Step(
            id = StepId.CALL_HOTLINES,
            title = R.string.step_call_hotlines,
            actions = listOf(StepAction.CallEmergencyContact),
        ),
        Step(
            id = StepId.PUT_WEAPONS_DOWN,
            title = R.string.step_put_weapons_down,
        ),
        Step(
            id = StepId.OBSERVE_AND_DESCRIBE,
            title = R.string.step_observe_and_describe,
            actions = listOf(StepAction.PlayAudio(R.raw.observe_and_describe_vf)),
        ),
        Step(
            id = StepId.LEAVES_ON_A_STREAM,
            title = R.string.step_leaves_on_a_stream,
            actions = listOf(StepAction.PlayAudio(R.raw.leaves_on_a_stream_vf)),
        ),
        Step(
            id = StepId.IDENTIFY_VALUES,
            title = R.string.step_identify_values,
        ),
        Step(
            id = StepId.MINDFUL_WALKING,
            title = R.string.step_mindful_walking,
            actions = listOf(StepAction.PlayAudio(R.raw.mindful_walking_vf)),
        ),
        Step(
            id = StepId.PROGRESSIVE_MUSCLE_RELAXATION,
            title = R.string.step_progressive_muscle_relaxation,
            actions = listOf(StepAction.PlayAudio(R.raw.progressive_muscle_relaxing_vf)),
        ),
        Step(
            id = StepId.DISTRACT_YOURSELF,
            title = R.string.step_distract_yourself,
        ),
        Step(
            id = StepId.AWARENESS_OF_THOUGHT,
            title = R.string.step_awareness_of_thought,
            actions = listOf(StepAction.PlayAudio(R.raw.awareness_of_thoughts_vf)),
        ),
        Step(
            id = StepId.FOLLOW_THE_CIRCLE,
            title = R.string.step_follow_the_circle,
            actions = listOf(StepAction.Breathing(BreathingPattern.CIRCLE)),
        ),
        Step(
            id = StepId.URGE_SURFING,
            title = R.string.step_urge_surfing,
            actions = listOf(StepAction.PlayAudio(R.raw.urge_surfing_vf)),
        ),
        Step(
            id = StepId.SOBER,
            title = R.string.step_sober,
            body = R.string.step_sober_body,
        ),
        Step(
            id = StepId.FOUR_SQUARE_BREATHING,
            title = R.string.step_four_square_breathing,
            actions = listOf(
                StepAction.Breathing(BreathingPattern.SQUARE),
                StepAction.PlayAudio(R.raw.four_square_breathing_vf),
            ),
        ),
        Step(
            id = StepId.DIAPHRAGMATIC_BREATHING,
            title = R.string.step_diaphragmatic_breathing,
            actions = listOf(StepAction.PlayAudio(R.raw.diagramic_breathing_vf)),
        ),
        Step(
            id = StepId.FIVE_SENSES,
            title = R.string.step_five_senses,
            actions = listOf(StepAction.PlayAudio(R.raw.five_senses_vf)),
        ),
        Step(
            id = StepId.MOUNTAIN_MEDITATION,
            title = R.string.step_mountain_meditation,
            actions = listOf(StepAction.PlayAudio(R.raw.mountian_meditation_vf)),
        ),
        Step(
            id = StepId.DISTRACTIONS,
            title = R.string.step_distractions,
            actions = listOf(StepAction.Distractions),
        ),
        Step(
            id = StepId.WHOLE_BODY_BREATHING,
            title = R.string.step_whole_body_breathing,
            actions = listOf(StepAction.PlayAudio(R.raw.whole_body_breathing_meditation_vf)),
        ),
        Step(
            id = StepId.BREATHING_SPACE,
            title = R.string.step_breathing_space,
            actions = listOf(StepAction.PlayAudio(R.raw.breathing_space_vf)),
        ),
        Step(
            id = StepId.BREATHING_BODY,
            title = R.string.step_breathing_body,
            actions = listOf(StepAction.PlayAudio(R.raw.breathing_body_vf)),
        ),
    )

    private val byId: Map<StepId, Step> = steps.associateBy { it.id }

    operator fun get(id: StepId): Step =
        byId.getValue(id)

    fun getOrNull(id: StepId): Step? = byId[id]

    /**
     * The order a veteran starts with before a clinician tailors it. This mirrors the
     * "S.O.P. for SAFETY" example in the requirements, which is the SOP the app is
     * built around.
     */
    val defaultOrder: List<StepId> = listOf(
        StepId.DEEP_BREATH,
        StepId.REASONS_FOR_LIVING,
        StepId.HALT,
        StepId.TWO_MINUTE_MINDFUL_BREATHING,
        StepId.BODY_SCAN,
        StepId.LISTEN_TO_MUSIC,
        StepId.CALL_SOMEONE,
        StepId.CALL_HOTLINES,
    )
}
