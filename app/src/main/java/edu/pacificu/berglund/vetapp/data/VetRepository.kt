package edu.pacificu.berglund.vetapp.data

import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.Step
import edu.pacificu.berglund.vetapp.domain.StepId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * The app's single source of truth for the veteran's profile and safety plan.
 *
 * Note the branch is read from and written to the database only. It previously lived in
 * *both* the database and a `SharedPreferences` entry, written by different screens and
 * read by different screens, so the two could and did disagree.
 */
class VetRepository(private val dao: VetDao) {

    /** Null until first-run setup completes. */
    val user: Flow<VetUser?> = dao.observeUser().map { it?.repaired() }

    /** The resolved safety plan, ready to render. */
    val safetyPlan: Flow<List<Step>> = user.map { current ->
        (current?.stepOrder ?: StepCatalog.defaultOrder).map(StepCatalog::get)
    }

    suspend fun currentUser(): VetUser? = dao.getUser()?.repaired()

    suspend fun createUser(branch: Branch, emergencyContact: String) {
        if (dao.count() > 0) return
        dao.insert(
            VetUser(
                branch = branch,
                emergencyContact = emergencyContact,
                stepOrder = StepCatalog.defaultOrder,
            ),
        )
    }

    suspend fun setBranch(branch: Branch) = updateUser { it.copy(branch = branch) }

    suspend fun setEmergencyContact(number: String) =
        updateUser { it.copy(emergencyContact = number) }

    suspend fun setMusicPreference(preference: MusicPreference) =
        updateUser { it.copy(musicPreference = preference) }

    /**
     * Replaces the safety plan. Sanitised before it is stored, so the corruption the old
     * reorder screen produced — it appended to its index list on every `onPause` without
     * ever clearing it, growing 10 → 20 → 30 entries with duplicates — cannot recur.
     */
    suspend fun setStepOrder(order: List<StepId>) =
        updateUser { it.copy(stepOrder = sanitise(order)) }

    private suspend fun updateUser(transform: (VetUser) -> VetUser) {
        val current = dao.getUser() ?: return
        dao.update(transform(current))
    }

    private fun VetUser.repaired(): VetUser {
        val clean = sanitise(stepOrder)
        return if (clean == stepOrder) this else copy(stepOrder = clean)
    }

    private companion object {

        /**
         * Drops duplicates and any step no longer in the catalog, and guarantees a
         * non-empty plan. A veteran must never open the app to zero steps.
         */
        fun sanitise(order: List<StepId>): List<StepId> {
            val clean = order.distinct().filter { StepCatalog.getOrNull(it) != null }
            return clean.ifEmpty { StepCatalog.defaultOrder }
        }
    }
}
