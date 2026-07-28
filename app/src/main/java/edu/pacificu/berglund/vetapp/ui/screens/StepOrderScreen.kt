package edu.pacificu.berglund.vetapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.data.StepCatalog
import edu.pacificu.berglund.vetapp.domain.StepId
import edu.pacificu.berglund.vetapp.ui.components.CrisisButton
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Editor for the veteran's safety plan.
 *
 * This replaces a screen with ten hardcoded `Spinner`s resolved by
 * `getIdentifier("spinner" + i, "id", packageName)`, which had two defects worth naming
 * because this design removes both by construction:
 *
 *  - It always wrote exactly ten steps, silently discarding the other eighteen. The
 *    requirements call for the veteran and clinician to choose about eight.
 *  - Its index list was appended to and never cleared, including from `onPause`, so
 *    backgrounding the screen and returning grew the saved plan to twenty entries with
 *    duplicates.
 *
 * Here the plan is a set with explicit add and remove, so it cannot contain duplicates
 * and cannot grow on its own. Reordering is via labelled up/down buttons rather than a
 * drag gesture — the audience is in crisis and may be older, and a drag target is the
 * least forgiving control there is.
 */
@Composable
fun StepOrderScreen(
    initialOrder: List<StepId>,
    onSave: (List<StepId>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val plan = remember(initialOrder) { mutableStateListOf<StepId>().apply { addAll(initialOrder) } }
    var showEmptyWarning by remember { mutableStateOf(false) }

    val available = StepCatalog.steps.map { it.id }.filterNot { it in plan }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        Text(
            text = stringResource(R.string.order_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.order_explain),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (showEmptyWarning) {
            Text(
                text = stringResource(R.string.order_min_one),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SectionHeading(stringResource(R.string.order_in_plan))
            }

            itemsIndexed(plan, key = { _, id -> "plan-$id" }) { index, id ->
                PlanRow(
                    position = index + 1,
                    label = stringResource(StepCatalog[id].title),
                    canMoveUp = index > 0,
                    canMoveDown = index < plan.lastIndex,
                    onMoveUp = { plan.swap(index, index - 1) },
                    onMoveDown = { plan.swap(index, index + 1) },
                    onRemove = { plan.removeAt(index) },
                )
            }

            item {
                SectionHeading(stringResource(R.string.order_available))
            }

            items(available, key = { "available-$it" }) { id ->
                AvailableRow(
                    label = stringResource(StepCatalog[id].title),
                    onAdd = { plan.add(id) },
                )
            }
        }

        CrisisButton(
            text = stringResource(R.string.order_save),
            onClick = {
                if (plan.isEmpty()) {
                    showEmptyWarning = true
                } else {
                    onSave(plan.toList())
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = Dimens.ContentSpacing),
    )
}

@Composable
private fun PlanRow(
    position: Int,
    label: String,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = Dimens.MinTouchTarget)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$position.",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            IconAction(
                icon = Icons.Filled.KeyboardArrowUp,
                description = stringResource(R.string.order_move_up),
                enabled = canMoveUp,
                onClick = onMoveUp,
            )
            IconAction(
                icon = Icons.Filled.KeyboardArrowDown,
                description = stringResource(R.string.order_move_down),
                enabled = canMoveDown,
                onClick = onMoveDown,
            )
            IconAction(
                icon = Icons.Filled.Close,
                description = stringResource(R.string.order_remove),
                enabled = true,
                onClick = onRemove,
            )
        }
    }
}

@Composable
private fun AvailableRow(label: String, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.MinTouchTarget)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        IconAction(
            icon = Icons.Filled.Add,
            description = stringResource(R.string.order_add),
            enabled = true,
            onClick = onAdd,
        )
    }
}

@Composable
private fun IconAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(Dimens.MinTouchTarget),
    ) {
        Icon(
            imageVector = icon,
            // Never icon-only without a label for screen readers.
            contentDescription = description,
            modifier = Modifier.size(28.dp),
        )
    }
}

private fun <T> androidx.compose.runtime.snapshots.SnapshotStateList<T>.swap(a: Int, b: Int) {
    if (a !in indices || b !in indices) return
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
}
