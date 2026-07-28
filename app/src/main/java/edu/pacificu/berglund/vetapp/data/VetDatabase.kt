package edu.pacificu.berglund.vetapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.StepId

@Database(entities = [VetUser::class], version = 3, exportSchema = true)
@TypeConverters(Converters::class)
abstract class VetDatabase : RoomDatabase() {

    abstract fun vetDao(): VetDao

    companion object {
        private const val NAME = "Vet-DB"

        @Volatile
        private var instance: VetDatabase? = null

        fun getInstance(context: Context): VetDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context).also { instance = it }
            }

        private fun build(context: Context): VetDatabase =
            Room.databaseBuilder(context.applicationContext, VetDatabase::class.java, NAME)
                // Deliberately NOT fallbackToDestructiveMigration(). This table holds a
                // clinician-configured safety plan; silently wiping it on a schema change
                // is the worst possible failure mode for this app.
                .addMigrations(MIGRATION_2_3)
                .build()

        /**
         * v2 → v3. The column layout is unchanged; the *encoding* of two columns is.
         *
         *  - `mcMusicPreference` held one of three inconsistent spellings
         *    (`"SPOTIFY"`/`"Spotify"`/`"LOCAL"`/`"Local"`…) depending on which screen
         *    wrote it. Normalised to the canonical [MusicPreference.storedValue].
         *  - `mcPageIndexes` held comma-separated *positional indices* into a hardcoded
         *    28-step list. Rewritten as [StepId] names so the catalog can change without
         *    rewriting anyone's safety plan.
         *
         * Duplicates left behind by the old reorder screen (which appended to its index
         * list without ever clearing it) are collapsed here.
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.query("SELECT UID, mcBranch, mcMusicPreference, mcPageIndexes FROM VetUser")
                    .use { cursor ->
                        while (cursor.moveToNext()) {
                            val uid = cursor.getInt(0)
                            val branch = Branch.fromStored(cursor.getStringOrNull(1))
                            val music = MusicPreference.fromStored(cursor.getStringOrNull(2))
                            val order = migrateStepOrder(cursor.getStringOrNull(3))

                            db.execSQL(
                                "UPDATE VetUser SET mcBranch = ?, mcMusicPreference = ?, " +
                                    "mcPageIndexes = ? WHERE UID = ?",
                                arrayOf(
                                    branch.storedValue,
                                    music?.storedValue,
                                    order.joinToString(",") { it.name },
                                    uid,
                                ),
                            )
                        }
                    }
            }
        }

        /**
         * Legacy orders were positional indices into the old hardcoded list. [StepCatalog]
         * preserves that exact ordering, so index *i* maps to `StepCatalog.steps[i]`.
         */
        private fun migrateStepOrder(raw: String?): List<StepId> {
            if (raw.isNullOrBlank()) return StepCatalog.defaultOrder
            val catalog = StepCatalog.steps
            val migrated = raw.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .mapNotNull { index -> catalog.getOrNull(index)?.id }
                .distinct()
            return migrated.ifEmpty { StepCatalog.defaultOrder }
        }

        private fun android.database.Cursor.getStringOrNull(index: Int): String? =
            if (isNull(index)) null else getString(index)
    }
}
