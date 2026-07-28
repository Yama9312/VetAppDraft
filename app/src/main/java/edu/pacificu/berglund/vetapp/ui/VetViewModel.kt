package edu.pacificu.berglund.vetapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.pacificu.berglund.vetapp.data.StepCatalog
import edu.pacificu.berglund.vetapp.data.VetRepository
import edu.pacificu.berglund.vetapp.data.VetUser
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.Step
import edu.pacificu.berglund.vetapp.domain.StepId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Where the app is in first-run setup, if anywhere.
 */
sealed interface StartDestination {
    data object Loading : StartDestination
    data object NeedsProfile : StartDestination
    data object NeedsMusicChoice : StartDestination
    data object Ready : StartDestination
}

data class SopState(
    val steps: List<Step> = emptyList(),
    val index: Int = 0,
) {
    val current: Step? get() = steps.getOrNull(index)
    val isFirst: Boolean get() = index <= 0
    val isLast: Boolean get() = index >= steps.lastIndex
    val humanPosition: Int get() = index + 1
}

/**
 * One ViewModel, scoped to the activity, shared by every screen.
 *
 * That scoping is the fix for a specific defect: the step position used to be a fragment
 * argument, so `SettingsFragment` and `EmergencyFragment` both returned to the SOP by
 * constructing `new DynamicPageFragment()` with no arguments — index 0. A veteran who hit
 * the emergency button at step 9 restarted their whole safety plan. Holding the index
 * here means leaving and returning resumes exactly where they were, which is what the
 * requirements' open question about return behaviour should resolve to for a crisis tool.
 *
 * It also survives rotation for free. The old code had no `onSaveInstanceState`, no
 * `ViewModel`, and committed fragments from background threads — which is where the
 * random `IllegalStateException`s in the git history came from.
 */
class VetViewModel(private val repository: VetRepository) : ViewModel() {

    private val stepIndex = MutableStateFlow(0)

    val user: StateFlow<VetUser?> = repository.user
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _hasLoaded = MutableStateFlow(false)
    val hasLoaded: StateFlow<Boolean> = _hasLoaded.asStateFlow()

    val startDestination: StateFlow<StartDestination> =
        combine(repository.user, _hasLoaded) { current, loaded ->
            when {
                !loaded -> StartDestination.Loading
                current == null -> StartDestination.NeedsProfile
                current.musicPreference == null -> StartDestination.NeedsMusicChoice
                else -> StartDestination.Ready
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StartDestination.Loading)

    val sop: StateFlow<SopState> =
        combine(repository.safetyPlan, stepIndex) { steps, index ->
            // Clamp rather than trust: the stored plan can shrink when a clinician edits
            // it while the veteran is part-way through.
            SopState(steps = steps, index = index.coerceIn(0, maxOf(0, steps.lastIndex)))
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SopState())

    /**
     * Branch chosen during first-run setup, before a profile row exists to write it to.
     * Once the profile is created the stored value takes over.
     */
    private val pendingBranch = MutableStateFlow(Branch.DEFAULT)

    val branch: StateFlow<Branch> =
        combine(repository.user, pendingBranch) { current, pending -> current?.branch ?: pending }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Branch.DEFAULT)

    init {
        viewModelScope.launch {
            repository.currentUser()
            _hasLoaded.value = true
        }
    }

    fun next() {
        val state = sop.value
        if (!state.isLast) stepIndex.value = state.index + 1
    }

    fun previous() {
        val state = sop.value
        if (!state.isFirst) stepIndex.value = state.index - 1
    }

    /** Restart the SOP from the top — used by the end-of-plan "go again" action. */
    fun restart() {
        stepIndex.value = 0
    }

    fun createProfile(branch: Branch, emergencyContact: String) = viewModelScope.launch {
        repository.createUser(branch, emergencyContact)
    }

    /**
     * Applies immediately so the palette previews as the veteran chooses, whether or not
     * a profile exists yet.
     */
    fun setBranch(branch: Branch) {
        pendingBranch.value = branch
        viewModelScope.launch { repository.setBranch(branch) }
    }

    fun setMusicPreference(preference: MusicPreference) = viewModelScope.launch {
        repository.setMusicPreference(preference)
    }

    fun setStepOrder(order: List<StepId>) = viewModelScope.launch {
        repository.setStepOrder(order)
        // Keep the veteran on a valid step if the plan just got shorter.
        stepIndex.value = stepIndex.value.coerceAtMost(maxOf(0, order.size - 1))
    }

    val catalog: List<Step> get() = StepCatalog.steps

    class Factory(private val repository: VetRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            VetViewModel(repository) as T
    }
}
