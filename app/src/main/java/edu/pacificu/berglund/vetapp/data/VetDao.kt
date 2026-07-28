package edu.pacificu.berglund.vetapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data access for the single local profile.
 *
 * Everything is `suspend` or a [Flow]. The previous DAO was blocking, which is why every
 * call site wrapped it in a bare `new Thread { … runOnUiThread { … } }` — 27 of them —
 * and why a fragment that went away before its query returned crashed on
 * `requireActivity()`.
 */
@Dao
interface VetDao {

    /** Emits on every write, so the UI never has to re-query by hand. */
    @Query("SELECT * FROM VetUser ORDER BY UID LIMIT 1")
    fun observeUser(): Flow<VetUser?>

    @Query("SELECT * FROM VetUser ORDER BY UID LIMIT 1")
    suspend fun getUser(): VetUser?

    @Query("SELECT COUNT(*) FROM VetUser")
    suspend fun count(): Int

    @Insert
    suspend fun insert(user: VetUser): Long

    @Update
    suspend fun update(user: VetUser)

    @Query("DELETE FROM VetUser")
    suspend fun deleteAll()
}
