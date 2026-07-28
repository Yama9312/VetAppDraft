package edu.pacificu.berglund.vetapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.ui.components.CrisisButton
import edu.pacificu.berglund.vetapp.ui.components.EmergencyButton
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Reached from "Finish" on the last step.
 *
 * Deliberately does not congratulate or close the app. Working the plan may not have been
 * enough, so the two things on offer are: go through it again, or get help now.
 */
@Composable
fun EndScreen(
    onGoAgain: () -> Unit,
    onGetHelp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.end_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(R.string.end_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.ContentSpacing),
        )

        CrisisButton(
            text = stringResource(R.string.end_go_again),
            onClick = onGoAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SectionSpacing),
        )

        EmergencyButton(
            text = stringResource(R.string.emergency_title),
            onClick = onGetHelp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.ContentSpacing),
        )
    }
}
