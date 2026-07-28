package edu.pacificu.berglund.vetapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.ui.components.CrisisOutlinedButton
import edu.pacificu.berglund.vetapp.ui.components.EmergencyButton
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Reachable from every step, one tap away.
 *
 * The previous version rendered these as bare `TextView`s about 24dp tall in
 * `@android:color/holo_blue_dark` on cream — roughly 2.6:1 contrast, well under WCAG AA,
 * in tap targets half the minimum size, on the one screen where a veteran in crisis
 * cannot afford to miss. They are now full-width buttons at [Dimens.PrimaryActionHeight]
 * using theme colours the contrast test holds to AA.
 *
 * The old screen also shipped a placeholder non-emergent police number,
 * "123-456-7890", which would have dialled a stranger. It is gone; a configurable
 * secondary contact is listed as unimplemented in the README rather than faked.
 */
@Composable
fun EmergencyScreen(
    emergencyContact: String?,
    onReturn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.emergency_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        EmergencyButton(
            text = stringResource(R.string.emergency_ems),
            onClick = { context.dialNumber("911") },
            modifier = Modifier.fillMaxWidth(),
        )
        Caption(stringResource(R.string.emergency_ems_detail))

        // 988 press 1 is the current Veterans Crisis Line. The previous build still
        // carried the pre-2022 Lifeline number, 1-800-273-8255, in strings.xml.
        EmergencyButton(
            text = stringResource(R.string.emergency_crisis_call),
            onClick = { context.dialNumber("988") },
            modifier = Modifier.fillMaxWidth(),
        )
        Caption(stringResource(R.string.emergency_crisis_call_detail))

        // The requirements say "call/text Veteran's crisis line" — both, not just voice.
        CrisisOutlinedButton(
            text = stringResource(R.string.emergency_crisis_text),
            onClick = { context.textNumber("838255") },
            modifier = Modifier.fillMaxWidth(),
        )
        Caption(stringResource(R.string.emergency_crisis_text_detail))

        if (!emergencyContact.isNullOrBlank()) {
            CrisisOutlinedButton(
                text = stringResource(R.string.emergency_contact, emergencyContact.formatPhone()),
                onClick = { context.dialNumber(emergencyContact) },
                modifier = Modifier.fillMaxWidth(),
            )
            Caption(stringResource(R.string.emergency_contact_detail))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = Dimens.MinTouchTarget)
                .padding(top = Dimens.SectionSpacing),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            CrisisOutlinedButton(
                text = stringResource(R.string.emergency_return),
                onClick = onReturn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
        }
    }
}

@Composable
private fun Caption(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private fun android.content.Context.dialNumber(number: String) {
    runCatching { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))) }
}

private fun android.content.Context.textNumber(shortCode: String) {
    runCatching { startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$shortCode"))) }
}

/** Digits only in storage; grouped for display. */
private fun String.formatPhone(): String {
    val digits = filter(Char::isDigit)
    return if (digits.length == 10) {
        "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6)}"
    } else {
        this
    }
}
