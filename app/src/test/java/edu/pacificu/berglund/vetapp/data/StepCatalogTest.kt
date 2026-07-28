package edu.pacificu.berglund.vetapp.data

import edu.pacificu.berglund.vetapp.domain.StepId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StepCatalogTest {

    /**
     * Load-bearing for `VetDatabase.MIGRATION_2_3`.
     *
     * Existing installs store their safety plan as positional indices into the old
     * hardcoded 28-step list in `MainActivity`. The migration maps index *i* to
     * `StepCatalog.steps[i]`, so this order must stay byte-identical to the legacy one.
     * If a step is ever inserted or moved, the migration has to be rewritten to pin the
     * old order separately — not this list changed.
     */
    @Test
    fun `catalog order matches the legacy hardcoded order`() {
        val legacyOrder = listOf(
            StepId.DEEP_BREATH,
            StepId.REASONS_FOR_LIVING,
            StepId.HALT,
            StepId.TWO_MINUTE_MINDFUL_BREATHING,
            StepId.SAFETY_SOP,
            StepId.BODY_SCAN,
            StepId.LISTEN_TO_MUSIC,
            StepId.CALL_SOMEONE,
            StepId.CALL_HOTLINES,
            StepId.PUT_WEAPONS_DOWN,
            StepId.OBSERVE_AND_DESCRIBE,
            StepId.LEAVES_ON_A_STREAM,
            StepId.IDENTIFY_VALUES,
            StepId.MINDFUL_WALKING,
            StepId.PROGRESSIVE_MUSCLE_RELAXATION,
            StepId.DISTRACT_YOURSELF,
            StepId.AWARENESS_OF_THOUGHT,
            StepId.FOLLOW_THE_CIRCLE,
            StepId.URGE_SURFING,
            StepId.SOBER,
            StepId.FOUR_SQUARE_BREATHING,
            StepId.DIAPHRAGMATIC_BREATHING,
            StepId.FIVE_SENSES,
            StepId.MOUNTAIN_MEDITATION,
            StepId.DISTRACTIONS,
            StepId.WHOLE_BODY_BREATHING,
            StepId.BREATHING_SPACE,
            StepId.BREATHING_BODY,
        )

        assertEquals(legacyOrder, StepCatalog.steps.map { it.id })
    }

    @Test
    fun `every catalog entry is reachable by id`() {
        StepId.entries.forEach { id ->
            assertEquals(id, StepCatalog[id].id)
        }
    }

    @Test
    fun `catalog has no duplicate ids`() {
        val ids = StepCatalog.steps.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `default order is a non-empty subset of the catalog`() {
        assertTrue(StepCatalog.defaultOrder.isNotEmpty())
        assertTrue(StepCatalog.defaultOrder.all { StepCatalog.getOrNull(it) != null })
        assertEquals(
            StepCatalog.defaultOrder.size,
            StepCatalog.defaultOrder.distinct().size,
        )
    }
}
