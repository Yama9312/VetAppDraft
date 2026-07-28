package edu.pacificu.berglund.vetapp.data

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import edu.pacificu.berglund.vetapp.domain.StepId
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Proves the v2 -> v3 migration preserves a veteran's safety plan.
 *
 * This is the test that justifies removing `fallbackToDestructiveMigration()`. Under the
 * old configuration every one of these rows would simply have been deleted on upgrade,
 * taking the clinician-configured plan with it.
 *
 * Needs a device or emulator: `./gradlew connectedDebugAndroidTest`.
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        VetDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrate2To3_rewritesPositionalIndicesAsStepIds() {
        helper.createDatabase(TEST_DB, 2).use { db ->
            // Legacy row: casing as the old first-run screen wrote it, and a step order
            // stored as positional indices into the old hardcoded 28-step list.
            db.execSQL(
                "INSERT INTO VetUser (UID, mcBranch, mcEContact, mcMusicPreference, mcPageIndexes) " +
                    "VALUES (1, 'Marine Corps', '5035550000', 'SPOTIFY', '0,2,5')",
            )
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, true, VetDatabase.MIGRATION_2_3)

        val db = VetDatabase.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext,
        )
        val migrated = requireNotNull(runBlockingGetUser(db))

        // Indices 0, 2 and 5 are the 1st, 3rd and 6th catalog entries.
        assertEquals(
            listOf(StepId.DEEP_BREATH, StepId.HALT, StepId.BODY_SCAN),
            migrated.stepOrder,
        )
    }

    @Test
    fun migrate2To3_normalisesMusicPreferenceCasing() {
        helper.createDatabase(TEST_DB, 2).use { db ->
            db.execSQL(
                "INSERT INTO VetUser (UID, mcBranch, mcEContact, mcMusicPreference, mcPageIndexes) " +
                    "VALUES (1, 'Army', '5035550000', 'SPOTIFY', '0')",
            )
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DB, 3, true, VetDatabase.MIGRATION_2_3,
        )

        migrated.query("SELECT mcMusicPreference FROM VetUser").use { cursor ->
            cursor.moveToFirst()
            // The old app wrote "SPOTIFY" here and read "Spotify" elsewhere, so the
            // veteran's choice was silently ignored. One canonical spelling now.
            assertEquals("Spotify", cursor.getString(0))
        }
    }

    private fun runBlockingGetUser(db: VetDatabase): VetUser? =
        kotlinx.coroutines.runBlocking { db.vetDao().getUser() }

    private companion object {
        const val TEST_DB = "migration-test"
    }
}
