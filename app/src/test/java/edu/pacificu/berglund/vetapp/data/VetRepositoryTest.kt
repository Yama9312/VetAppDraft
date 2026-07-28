package edu.pacificu.berglund.vetapp.data

import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.StepId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Covers the sanitising behaviour that makes the old step-order corruption unrecoverable
 * *and* unreachable: even a database row already holding a corrupted plan is repaired on
 * read.
 */
class VetRepositoryTest {

    private class FakeDao(initial: VetUser? = null) : VetDao {
        val state = MutableStateFlow(initial)
        override fun observeUser(): Flow<VetUser?> = state
        override suspend fun getUser(): VetUser? = state.value
        override suspend fun count(): Int = if (state.value == null) 0 else 1
        override suspend fun insert(user: VetUser): Long {
            state.value = user
            return 1
        }

        override suspend fun update(user: VetUser) {
            state.value = user
        }

        override suspend fun deleteAll() {
            state.value = null
        }
    }

    @Test
    fun `duplicate steps are collapsed on read`() = runTest {
        // Exactly the shape the old reorder screen produced: it appended to its index
        // list on every onPause without clearing, so a saved plan could repeat itself.
        val corrupted = VetUser(
            uid = 1,
            stepOrder = listOf(
                StepId.DEEP_BREATH,
                StepId.HALT,
                StepId.DEEP_BREATH,
                StepId.HALT,
            ),
        )
        val repository = VetRepository(FakeDao(corrupted))

        assertEquals(
            listOf(StepId.DEEP_BREATH, StepId.HALT),
            repository.user.first()!!.stepOrder,
        )
    }

    @Test
    fun `an empty plan falls back to the default rather than showing nothing`() = runTest {
        val repository = VetRepository(FakeDao(VetUser(uid = 1, stepOrder = emptyList())))

        val order = repository.user.first()!!.stepOrder
        assertTrue(order.isNotEmpty())
        assertEquals(StepCatalog.defaultOrder, order)
    }

    @Test
    fun `saving a plan strips duplicates`() = runTest {
        val dao = FakeDao(VetUser(uid = 1))
        val repository = VetRepository(dao)

        repository.setStepOrder(
            listOf(StepId.HALT, StepId.DEEP_BREATH, StepId.HALT),
        )

        assertEquals(listOf(StepId.HALT, StepId.DEEP_BREATH), dao.state.value!!.stepOrder)
    }

    @Test
    fun `safety plan resolves to real steps in the saved order`() = runTest {
        val repository = VetRepository(
            FakeDao(VetUser(uid = 1, stepOrder = listOf(StepId.HALT, StepId.DEEP_BREATH))),
        )

        assertEquals(
            listOf(StepId.HALT, StepId.DEEP_BREATH),
            repository.safetyPlan.first().map { it.id },
        )
    }

    @Test
    fun `music preference round-trips through the repository`() = runTest {
        val dao = FakeDao(VetUser(uid = 1))
        val repository = VetRepository(dao)

        repository.setMusicPreference(MusicPreference.SPOTIFY)

        assertEquals(MusicPreference.SPOTIFY, dao.state.value!!.musicPreference)
    }

    @Test
    fun `createUser is a no-op when a profile already exists`() = runTest {
        val dao = FakeDao(VetUser(uid = 1, branch = Branch.NAVY))
        val repository = VetRepository(dao)

        repository.createUser(Branch.ARMY, "5035550000")

        assertEquals(Branch.NAVY, dao.state.value!!.branch)
    }
}
