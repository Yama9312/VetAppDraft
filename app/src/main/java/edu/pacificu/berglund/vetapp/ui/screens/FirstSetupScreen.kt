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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.Branch
import edu.pacificu.berglund.vetapp.ui.components.CrisisButton
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * First run: pick a branch (which sets the palette) and save someone to call.
 *
 * Selecting a branch applies its palette live, so the veteran sees what they are choosing
 * rather than committing blind.
 */
@Composable
fun FirstSetupScreen(
    selectedBranch: Branch,
    onBranchChange: (Branch) -> Unit,
    onContinue: (Branch, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var phone by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val digits = phone.filter(Char::isDigit)
    val valid = digits.length == 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.setup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(R.string.setup_branch),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Column(Modifier.selectableGroup()) {
            Branch.entries.forEach { branch ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = Dimens.MinTouchTarget)
                        .selectable(
                            selected = branch == selectedBranch,
                            onClick = { onBranchChange(branch) },
                            role = Role.RadioButton,
                        )
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = branch == selectedBranch, onClick = null)
                    Text(
                        text = stringResource(branch.label),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.setup_contact),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = Dimens.ContentSpacing),
        )

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                showError = false
            },
            label = { Text(stringResource(R.string.setup_contact_hint)) },
            singleLine = true,
            isError = showError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )

        if (showError) {
            Text(
                text = stringResource(R.string.setup_contact_invalid),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        CrisisButton(
            text = stringResource(R.string.setup_continue),
            onClick = {
                // Stored as digits only so it is unambiguous to dial; formatted for
                // display at the point of use.
                if (valid) onContinue(selectedBranch, digits) else showError = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SectionSpacing),
        )
    }
}
