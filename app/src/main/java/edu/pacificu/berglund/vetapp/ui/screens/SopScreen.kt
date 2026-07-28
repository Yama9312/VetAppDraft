package edu.pacificu.berglund.vetapp.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.domain.Step
import edu.pacificu.berglund.vetapp.domain.StepAction
import edu.pacificu.berglund.vetapp.ui.SopState
import edu.pacificu.berglund.vetapp.ui.components.BreathingPacer
import edu.pacificu.berglund.vetapp.ui.components.CrisisButton
import edu.pacificu.berglund.vetapp.ui.components.CrisisOutlinedButton
import edu.pacificu.berglund.vetapp.ui.components.StepNavigationRow
import edu.pacificu.berglund.vetapp.ui.components.rememberAudioController
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * The one screen every step renders through.
 *
 * Reusing a single screen for all steps was the original design's best decision and the
 * wireframe requires it ("each step in the SOP will use same screen"). What changed is
 * that the screen is driven by the step's declared [StepAction]s instead of by string
 * comparisons against its display text.
 */
@Composable
fun SopScreen(
    state: SopState,
    branch: Branch,
    emergencyContact: String?,
    musicPreference: MusicPreference,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onExitRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val step = state.current ?: return
    val context = LocalContext.current
    var confirmExit by remember { mutableStateOf(false) }

    // Back on the first step asks before leaving. The old build replaced fragments with
    // no back stack, so the system back gesture silently killed the app mid-crisis.
    BackHandler(enabled = true) {
        if (state.isFirst) confirmExit = true else onPrevious()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.sop_step_position, state.humanPosition, state.steps.size),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        // A step is usually one short line. Centring it in the space between the position
        // indicator and the navigation buttons puts the text where the eye lands, rather
        // than stranding it at the top with the screen empty below it.
        //
        // The explicit minimum height is load-bearing: verticalScroll measures its child
        // with an infinite maximum, so a vertical arrangement would otherwise have no
        // bounds to centre within and would silently stay top-aligned. Forcing the column
        // to at least fill the viewport gives it something to centre in, while still
        // letting taller steps grow and scroll.
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.ContentSpacing,
                    Alignment.CenterVertically,
                ),
            ) {
                Text(
                    text = stringResource(step.title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                step.body?.let { body ->
                    Text(
                        text = stringResource(body),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                step.actions.forEach { action ->
                    StepActionControl(
                        action = action,
                        emergencyContact = emergencyContact,
                        musicPreference = musicPreference,
                        onDial = { number -> context.dial(number) },
                        onOpen = { uri -> context.openExternally(uri) },
                    )
                }
            }
        }

        StepNavigationRow(
            previousLabel = stringResource(R.string.sop_previous),
            nextLabel = stringResource(
                if (state.isLast) R.string.sop_finish else R.string.sop_next,
            ),
            onPrevious = onPrevious,
            onNext = onNext,
            previousEnabled = !state.isFirst,
        )

        Text(
            text = stringResource(branch.values),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    if (confirmExit) {
        AlertDialog(
            onDismissRequest = { confirmExit = false },
            title = { Text(stringResource(R.string.exit_title)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmExit = false
                    onExitRequest()
                }) { Text(stringResource(R.string.exit_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { confirmExit = false }) {
                    Text(stringResource(R.string.exit_cancel))
                }
            },
        )
    }
}

@Composable
private fun StepActionControl(
    action: StepAction,
    emergencyContact: String?,
    musicPreference: MusicPreference,
    onDial: (String) -> Unit,
    onOpen: (Uri) -> Unit,
) {
    when (action) {
        is StepAction.PlayAudio -> {
            val audio = rememberAudioController(action.res)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CrisisButton(
                    text = stringResource(
                        if (audio.isPlaying) R.string.sop_pause else R.string.sop_play,
                    ),
                    onClick = { audio.toggle() },
                    modifier = Modifier.fillMaxWidth(),
                )
                CrisisOutlinedButton(
                    text = stringResource(R.string.sop_repeat),
                    onClick = { audio.repeat() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        is StepAction.Breathing -> BreathingPacer(pattern = action.pattern)

        StepAction.CallEmergencyContact -> {
            if (emergencyContact.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.sop_no_contact),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                CrisisButton(
                    text = stringResource(R.string.sop_call_contact),
                    onClick = { onDial(emergencyContact) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Explicit tap. The old players called startActivity from onViewCreated, so
        // simply reaching the music step threw the veteran out of the app.
        StepAction.OpenMusic -> {
            val context = LocalContext.current
            CrisisButton(
                text = stringResource(R.string.sop_open_music),
                onClick = { context.openMusic(musicPreference) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        StepAction.Distractions -> Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CrisisOutlinedButton(
                text = stringResource(R.string.distraction_fidget),
                onClick = { onOpen(Uri.parse("https://ffffidget.com/")) },
                modifier = Modifier.fillMaxWidth(),
            )
            CrisisOutlinedButton(
                text = stringResource(R.string.distraction_math),
                onClick = {
                    onOpen(
                        Uri.parse(
                            "https://www.wolframalpha.com/problem-generator/quiz/" +
                                "?category=Arithmetic&topic=AddOrSubtractSummary",
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            CrisisOutlinedButton(
                text = stringResource(R.string.distraction_crossword),
                onClick = { onOpen(Uri.parse("https://www.boatloadpuzzles.com/playcrossword")) },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(R.string.distraction_leaves_app),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * Hands off to whichever app the veteran chose, preferring the installed app and falling
 * back to the browser. The manifest declares the two packages under `<queries>` — without
 * that, package-visibility filtering on Android 11+ makes the installed app invisible and
 * every hand-off silently degrades to the browser.
 */
private fun android.content.Context.openMusic(preference: MusicPreference) {
    val candidates = when (preference) {
        MusicPreference.SPOTIFY -> listOf(
            Intent(Intent.ACTION_VIEW, Uri.parse("spotify:")).setPackage("com.spotify.music"),
            Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com")),
        )

        MusicPreference.YOUTUBE -> listOf(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com"))
                .setPackage("com.google.android.youtube"),
            Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com")),
        )

        // No single "local music" app exists, so ask the system for anything that plays
        // audio and let the veteran pick.
        MusicPreference.LOCAL -> listOf(
            Intent(Intent.ACTION_VIEW).setType("audio/*"),
            Intent(Intent.ACTION_GET_CONTENT).setType("audio/*"),
        )
    }

    candidates.firstOrNull { it.resolveActivity(packageManager) != null }
        ?.let { runCatching { startActivity(it) } }
}

/**
 * ACTION_DIAL, never ACTION_CALL. It needs no permission, it cannot mis-dial without the
 * veteran confirming, and it matches the wireframe: "this screen will be the phone's
 * default screen for calling someone".
 */
private fun android.content.Context.dial(number: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
    runCatching { startActivity(intent) }
}

private fun android.content.Context.openExternally(uri: Uri) {
    runCatching { startActivity(Intent(Intent.ACTION_VIEW, uri)) }
        .onFailure { if (it !is ActivityNotFoundException) throw it }
}
