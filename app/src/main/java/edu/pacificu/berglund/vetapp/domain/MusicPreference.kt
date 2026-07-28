package edu.pacificu.berglund.vetapp.domain

/**
 * Where the "listen to music" step sends the veteran.
 *
 * This type exists to make a specific bug unrepresentable. The old code stored this as
 * a free `String`: the first-run screen wrote `"SPOTIFY"`, the settings screen wrote
 * `"Spotify"`, and the player compared against `"Spotify"` — so a veteran who chose
 * Spotify during setup silently got the local file player instead, and only re-picking
 * it in Settings ever fixed it.
 */
enum class MusicPreference(val storedValue: String) {
    LOCAL("Local"),
    SPOTIFY("Spotify"),
    YOUTUBE("YouTube"),
    ;

    companion object {
        val DEFAULT = LOCAL

        /** Case-insensitive so the three legacy spellings all resolve correctly. */
        fun fromStored(value: String?): MusicPreference? =
            value?.let { stored ->
                entries.firstOrNull { it.storedValue.equals(stored, ignoreCase = true) }
            }
    }
}
