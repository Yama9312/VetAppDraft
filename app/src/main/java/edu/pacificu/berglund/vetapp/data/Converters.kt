package edu.pacificu.berglund.vetapp.data

import androidx.room.TypeConverter
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.StepId

/**
 * Room type converters.
 *
 * Reads are deliberately tolerant: rows written by the previous version used
 * inconsistent casing (`"SPOTIFY"` from the setup screen, `"Spotify"` from settings), and
 * an unrecognised value should degrade to a sensible default rather than crash a veteran
 * mid-crisis.
 */
class Converters {

    @TypeConverter
    fun fromBranch(branch: Branch?): String? = branch?.storedValue

    @TypeConverter
    fun toBranch(value: String?): Branch = Branch.fromStored(value)

    @TypeConverter
    fun fromMusicPreference(preference: MusicPreference?): String? = preference?.storedValue

    @TypeConverter
    fun toMusicPreference(value: String?): MusicPreference? = MusicPreference.fromStored(value)

    @TypeConverter
    fun fromStepOrder(order: List<StepId>?): String =
        order.orEmpty().joinToString(SEPARATOR) { it.name }

    @TypeConverter
    fun toStepOrder(value: String?): List<StepId> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(SEPARATOR).mapNotNull { token ->
            // Unknown names are dropped rather than throwing — a step removed from the
            // catalog in a later release must not brick an existing install.
            runCatching { StepId.valueOf(token.trim()) }.getOrNull()
        }
    }

    private companion object {
        const val SEPARATOR = ","
    }
}
