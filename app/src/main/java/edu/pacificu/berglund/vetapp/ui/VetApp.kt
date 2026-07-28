package edu.pacificu.berglund.vetapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.ui.screens.AppearanceScreen
import edu.pacificu.berglund.vetapp.ui.screens.EmergencyScreen
import edu.pacificu.berglund.vetapp.ui.screens.EndScreen
import edu.pacificu.berglund.vetapp.ui.screens.FirstSetupScreen
import edu.pacificu.berglund.vetapp.ui.screens.MusicPreferenceScreen
import edu.pacificu.berglund.vetapp.ui.screens.SettingsScreen
import edu.pacificu.berglund.vetapp.ui.screens.SopScreen
import edu.pacificu.berglund.vetapp.ui.screens.StepOrderScreen
import edu.pacificu.berglund.vetapp.ui.theme.Dimens
import edu.pacificu.berglund.vetapp.ui.theme.VetTheme

object Routes {
    const val SETUP = "setup"
    const val MUSIC_SETUP = "music-setup"
    const val SOP = "sop"
    const val END = "end"
    const val EMERGENCY = "emergency"
    const val SETTINGS = "settings"
    const val STEP_ORDER = "settings/step-order"
    const val APPEARANCE = "settings/appearance"
    const val MUSIC = "settings/music"
}

@Composable
fun VetApp(
    viewModel: VetViewModel,
    onExit: () -> Unit,
) {
    val branch by viewModel.branch.collectAsStateWithLifecycle()

    VetTheme(branch = branch) {
        val navController = rememberNavController()
        val start by viewModel.startDestination.collectAsStateWithLifecycle()

        when (start) {
            StartDestination.Loading -> Unit
            else -> VetScaffold(
                navController = navController,
                viewModel = viewModel,
                startRoute = when (start) {
                    StartDestination.NeedsProfile -> Routes.SETUP
                    StartDestination.NeedsMusicChoice -> Routes.MUSIC_SETUP
                    else -> Routes.SOP
                },
                onExit = onExit,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VetScaffold(
    navController: NavHostController,
    viewModel: VetViewModel,
    startRoute: String,
    onExit: () -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route

    // The top bar carries the two things that must always be one tap away. It is hidden
    // during first-run setup, where there is no plan to get back to yet.
    val showChrome = route != Routes.SETUP && route != Routes.MUSIC_SETUP

    val user by viewModel.user.collectAsStateWithLifecycle()
    val sop by viewModel.sop.collectAsStateWithLifecycle()
    val branch by viewModel.branch.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            if (showChrome) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.top_of_the_screen_slogan),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateOnce(Routes.EMERGENCY) },
                            modifier = Modifier.size(Dimens.MinTouchTarget),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = stringResource(R.string.nav_emergency),
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(30.dp),
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navController.navigateOnce(Routes.SETTINGS) },
                            modifier = Modifier.size(Dimens.MinTouchTarget),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(R.string.nav_settings),
                                modifier = Modifier.size(30.dp),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }
        },
    ) { insets ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(insets),
        ) {
            composable(Routes.SETUP) {
                FirstSetupScreen(
                    selectedBranch = branch,
                    onBranchChange = viewModel::setBranch,
                    onContinue = { chosen, contact ->
                        viewModel.createProfile(chosen, contact)
                        navController.navigate(Routes.MUSIC_SETUP) {
                            popUpTo(Routes.SETUP) { inclusive = true }
                        }
                    },
                )
            }

            composable(Routes.MUSIC_SETUP) {
                MusicPreferenceScreen(
                    selected = user?.musicPreference,
                    confirmLabel = stringResource(R.string.music_continue),
                    onSelect = viewModel::setMusicPreference,
                    onConfirm = {
                        navController.navigate(Routes.SOP) {
                            popUpTo(Routes.MUSIC_SETUP) { inclusive = true }
                        }
                    },
                )
            }

            composable(Routes.SOP) {
                SopScreen(
                    state = sop,
                    branch = branch,
                    emergencyContact = user?.emergencyContact,
                    musicPreference = user?.musicPreference ?: MusicPreference.DEFAULT,
                    onPrevious = viewModel::previous,
                    // Advancing past the last step lands on the end screen rather than
                    // wrapping silently back to step one, which is what the old build did.
                    onNext = {
                        if (sop.isLast) navController.navigateOnce(Routes.END) else viewModel.next()
                    },
                    onExitRequest = onExit,
                )
            }

            composable(Routes.END) {
                EndScreen(
                    onGoAgain = {
                        viewModel.restart()
                        navController.popBackStack(Routes.SOP, inclusive = false)
                    },
                    onGetHelp = { navController.navigateOnce(Routes.EMERGENCY) },
                )
            }

            composable(Routes.EMERGENCY) {
                EmergencyScreen(
                    emergencyContact = user?.emergencyContact,
                    onReturn = { navController.popBackStack() },
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onStepOrder = { navController.navigateOnce(Routes.STEP_ORDER) },
                    onAppearance = { navController.navigateOnce(Routes.APPEARANCE) },
                    onMusic = { navController.navigateOnce(Routes.MUSIC) },
                    onReturn = { navController.popBackStack(Routes.SOP, inclusive = false) },
                )
            }

            composable(Routes.STEP_ORDER) {
                StepOrderScreen(
                    initialOrder = user?.stepOrder.orEmpty(),
                    onSave = {
                        viewModel.setStepOrder(it)
                        navController.popBackStack()
                    },
                )
            }

            composable(Routes.APPEARANCE) {
                AppearanceScreen(
                    selected = branch,
                    onSelect = viewModel::setBranch,
                    onDone = { navController.popBackStack() },
                )
            }

            composable(Routes.MUSIC) {
                MusicPreferenceScreen(
                    selected = user?.musicPreference,
                    confirmLabel = stringResource(R.string.music_save),
                    onSelect = viewModel::setMusicPreference,
                    onConfirm = { navController.popBackStack() },
                )
            }
        }
    }
}

/**
 * Navigate without stacking a second copy of a destination the veteran is already on —
 * tapping the emergency icon twice should not require two back presses to escape.
 */
private fun NavHostController.navigateOnce(route: String) {
    if (currentDestination?.route != route) navigate(route) { launchSingleTop = true }
}
