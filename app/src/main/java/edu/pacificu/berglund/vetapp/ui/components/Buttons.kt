package edu.pacificu.berglund.vetapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Buttons sized for someone in crisis.
 *
 * Every control in this app is a labelled button with a target at or above
 * [Dimens.MinTouchTarget] (56dp, above Material's 48dp). Nothing is gesture-only and
 * nothing is icon-only without a content description — the requirements are explicit
 * that the audience skews older and is under acute stress.
 */
@Composable
fun CrisisButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.heightIn(min = Dimens.PrimaryActionHeight),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CrisisOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.heightIn(min = Dimens.MinTouchTarget),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Emergency action. Uses the error role so it is visually distinct from every other
 * control on screen, and never used for anything else.
 */
@Composable
fun EmergencyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        modifier = modifier.heightIn(min = Dimens.PrimaryActionHeight),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

/** Back / forward pair used at the foot of every step. */
@Composable
fun StepNavigationRow(
    previousLabel: String,
    nextLabel: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    previousEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        CrisisOutlinedButton(
            text = previousLabel,
            onClick = onPrevious,
            enabled = previousEnabled,
            modifier = Modifier.weight(1f),
        )
        CrisisButton(
            text = nextLabel,
            onClick = onNext,
            modifier = Modifier.weight(1f),
        )
    }
}
