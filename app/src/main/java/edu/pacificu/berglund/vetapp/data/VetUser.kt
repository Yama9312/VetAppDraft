package edu.pacificu.berglund.vetapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.StepId

/**
 * The single local profile: the veteran's branch, emergency contact, music choice, and
 * the safety plan they built with their clinician.
 *
 * Table and column names are held to their original spellings so the existing database
 * migrates in place rather than being dropped. The Kotlin-side names are the readable
 * ones.
 */
@Entity(tableName = "VetUser")
data class VetUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UID")
    val uid: Int = 0,

    @ColumnInfo(name = "mcBranch")
    val branch: Branch = Branch.DEFAULT,

    @ColumnInfo(name = "mcEContact")
    val emergencyContact: String? = null,

    /** Null means the veteran has not been asked yet — that drives first-run routing. */
    @ColumnInfo(name = "mcMusicPreference")
    val musicPreference: MusicPreference? = null,

    /**
     * The veteran's ordered safety plan.
     *
     * Stored as [StepId] names. It previously held positional indices into a hardcoded
     * list, which meant any future edit to the step catalog would silently rewrite every
     * existing veteran's plan into a different set of steps.
     */
    @ColumnInfo(name = "mcPageIndexes")
    val stepOrder: List<StepId> = StepCatalog.defaultOrder,
)
