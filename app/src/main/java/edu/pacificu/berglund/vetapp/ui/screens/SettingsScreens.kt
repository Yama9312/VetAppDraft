package edu.pacificu.berglund.vetapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.domain.MusicPreference
import edu.pacificu.berglund.vetapp.ui.components.CrisisButton
import edu.pacificu.berglund.vetapp.ui.components.CrisisOutlinedButton
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Settings hub.
 *
 * Note there is no "Skill Toolbox" entry. The old layout rendered that button but its
 * click listener was commented out, so it did nothing at all — the Skill Toolbox is a
 * genuine unbuilt requirement, and it is recorded in the README's "not implemented"
 * section rather than shipped as a control that silently fails.
 */
@Composable
fun SettingsScreen(
    onStepOrder: () -> Unit,
    onAppearance: () -> Unit,
    onMusic: () -> Unit,
    onReturn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        CrisisOutlinedButton(
            text = stringResource(R.string.settings_step_order),
            onClick = onStepOrder,
            modifier = Modifier.fillMaxWidth(),
        )
        CrisisOutlinedButton(
            text = stringResource(R.string.settings_appearance),
            onClick = onAppearance,
            modifier = Modifier.fillMaxWidth(),
        )
        CrisisOutlinedButton(
            text = stringResource(R.string.settings_music),
            onClick = onMusic,
            modifier = Modifier.fillMaxWidth(),
        )

        CrisisButton(
            text = stringResource(R.string.settings_return),
            onClick = onReturn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SectionSpacing),
        )
    }
}

@Composable
fun AppearanceScreen(
    selected: Branch,
    onSelect: (Branch) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.appearance_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.appearance_explain),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(Modifier.selectableGroup()) {
            Branch.entries.forEach { branch ->
                ChoiceRow(
                    label = stringResource(branch.label),
                    selected = branch == selected,
                    // Applies immediately so the veteran sees the palette before saving.
                    onClick = { onSelect(branch) },
                )
            }
        }

        CrisisButton(
            text = stringResource(R.string.appearance_save),
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SectionSpacing),
        )
    }
}

/**
 * Shared by first-run setup and settings.
 *
 * One screen, one write path, one spelling of each value. The old app had two screens
 * writing this field with different casing — `"SPOTIFY"` at setup, `"Spotify"` in
 * settings — against a reader that only matched the latter.
 */
@Composable
fun MusicPreferenceScreen(
    selected: MusicPreference?,
    confirmLabel: String,
    onSelect: (MusicPreference) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var choice by remember(selected) { mutableStateOf(selected ?: MusicPreference.DEFAULT) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.music_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Column(Modifier.selectableGroup()) {
            MusicPreference.entries.forEach { preference ->
                ChoiceRow(
                    label = stringResource(preference.labelRes()),
                    selected = preference == choice,
                    onClick = { choice = preference },
                )
            }
        }

        CrisisButton(
            text = confirmLabel,
            onClick = {
                onSelect(choice)
                onConfirm()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SectionSpacing),
        )
    }
}

private fun MusicPreference.labelRes(): Int = when (this) {
    MusicPreference.LOCAL -> R.string.music_local
    MusicPreference.SPOTIFY -> R.string.music_spotify
    MusicPreference.YOUTUBE -> R.string.music_youtube
}

/**
 * A radio row whose whole width is the tap target, not just the 20dp dot, and which
 * reports itself to accessibility services as a single radio button rather than as a
 * control plus an unrelated label.
 */
@Composable
private fun ChoiceRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.MinTouchTarget)
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Null onClick: the row above owns the click, so the dot must not be separately
        // focusable.
        RadioButton(selected = selected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        )
    }
}
