package edu.pacificu.berglund.vetapp.domain

import androidx.annotation.StringRes
import edu.pacificu.berglund.vetapp.R

/**
 * Branch of service. Drives both the colour palette and the values shown in the step
 * footer.
 *
 * Replaces a bare `String` that was written in three places and compared in three more,
 * and the identical 30-line `switch` that appeared in `BaseActivity`,
 * `FirstSetupFragment` and `ChangeAppearanceFragment`.
 *
 * The stored name is pinned explicitly rather than relying on [Enum.name] so that
 * renaming a constant can never silently orphan existing rows.
 */
enum class Branch(
    val storedValue: String,
    @StringRes val label: Int,
    @StringRes val values: Int,
) {
    ARMY("Army", R.string.branch_army, R.string.values_army),
    MARINE_CORPS("Marine Corps", R.string.branch_marine_corps, R.string.values_marine_corps),
    NAVY("Navy", R.string.branch_navy, R.string.values_navy),
    AIR_FORCE("Air Force", R.string.branch_air_force, R.string.values_air_force),
    COAST_GUARD("Coast Guard", R.string.branch_coast_guard, R.string.values_coast_guard),
    NEUTRAL("Grey Scale", R.string.branch_neutral, R.string.values_neutral),
    ;

    companion object {
        val DEFAULT = NEUTRAL

        /**
         * Tolerant of the casing drift in legacy rows — the old code wrote branch names
         * from a spinner in one place and compared them literally in another.
         */
        fun fromStored(value: String?): Branch =
            entries.firstOrNull { it.storedValue.equals(value, ignoreCase = true) } ?: DEFAULT
    }
}
