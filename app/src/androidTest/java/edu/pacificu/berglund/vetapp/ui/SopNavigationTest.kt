package edu.pacificu.berglund.vetapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.pacificu.berglund.vetapp.data.StepCatalog
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.StepId
import edu.pacificu.berglund.vetapp.ui.screens.SopScreen
import edu.pacificu.berglund.vetapp.ui.theme.VetTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Needs a device or emulator: `./gradlew connectedDebugAndroidTest`.
 */
@RunWith(AndroidJUnit4::class)
class SopNavigationTest {

    @get:Rule
    val compose = createComposeRule()

    private val plan = listOf(StepId.DEEP_BREATH, StepId.HALT, StepId.BODY_SCAN)
        .map(StepCatalog::get)

    @Test
    fun stepPositionIsShownAndPreviousIsDisabledOnTheFirstStep() {
        compose.setContent {
            VetTheme(branch = Branch.NEUTRAL) {
                SopScreen(
                    state = SopState(steps = plan, index = 0),
                    branch = Branch.NEUTRAL,
                    emergencyContact = "5035550000",
                    musicPreference = MusicPreference.LOCAL,
                    onPrevious = {},
                    onNext = {},
                    onExitRequest = {},
                )
            }
        }

        compose.onNodeWithText("Step 1 of 3").assertIsDisplayed()
        compose.onNodeWithText("Take a deep breath").assertIsDisplayed()
    }

    /**
     * The last step offers "Finish" rather than "Next" so the veteran is not looped back
     * to step one without being told. The old build wrapped silently.
     */
    @Test
    fun lastStepOffersFinish() {
        compose.setContent {
            VetTheme(branch = Branch.NEUTRAL) {
                SopScreen(
                    state = SopState(steps = plan, index = plan.lastIndex),
                    branch = Branch.NEUTRAL,
                    emergencyContact = null,
                    musicPreference = MusicPreference.LOCAL,
                    onPrevious = {},
                    onNext = {},
                    onExitRequest = {},
                )
            }
        }

        compose.onNodeWithText("Finish").assertIsDisplayed()
    }

    @Test
    fun advancingReportsToTheCaller() {
        var advanced = false

        compose.setContent {
            VetTheme(branch = Branch.NEUTRAL) {
                SopScreen(
                    state = SopState(steps = plan, index = 0),
                    branch = Branch.NEUTRAL,
                    emergencyContact = null,
                    musicPreference = MusicPreference.LOCAL,
                    onPrevious = {},
                    onNext = { advanced = true },
                    onExitRequest = {},
                )
            }
        }

        compose.onNodeWithText("Next").performClick()
        assert(advanced)
    }
}
