package edu.pacificu.berglund.vetapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import edu.pacificu.berglund.vetapp.R
import edu.pacificu.berglund.vetapp.domain.BreathingPattern
import edu.pacificu.berglund.vetapp.ui.theme.Dimens

/**
 * Animated breathing pacers.
 *
 * These replace two GIF assets that were selected by comparing the step's *display text*
 * (`getContent().equalsIgnoreCase("follow the circle")`), which meant rewording a step
 * broke its animation and translating the app broke both. They are now driven by
 * [BreathingPattern] off the step's declared actions.
 *
 * Drawing them also means the timings are real rather than baked into a GIF, and they
 * scale to any screen and respect the theme.
 */
@Composable
fun BreathingPacer(
    pattern: BreathingPattern,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "breathing")
    val cycleMillis = pattern.totalSeconds * 1000

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = cycleMillis
                0f at 0 using LinearEasing
                1f at cycleMillis using LinearEasing
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "cycle",
    )

    val phase = pattern.phaseAt(progress)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.ContentSpacing),
    ) {
        val description = stringResource(phase.labelRes)

        when (pattern) {
            BreathingPattern.CIRCLE -> CirclePacer(
                scale = pattern.circleScaleAt(progress),
                modifier = Modifier
                    .sizeIn(maxWidth = Dimens.BreathingMaxSize, maxHeight = Dimens.BreathingMaxSize)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .semantics { contentDescription = description },
            )

            BreathingPattern.SQUARE -> SquarePacer(
                progress = progress,
                modifier = Modifier
                    .sizeIn(maxWidth = Dimens.BreathingMaxSize, maxHeight = Dimens.BreathingMaxSize)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .semantics { contentDescription = description },
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun CirclePacer(scale: Float, modifier: Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val fill = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val maxRadius = size.minDimension / 2f
        drawCircle(color = outline, radius = maxRadius, style = Stroke(width = 4.dp.toPx()))
        drawCircle(color = fill, radius = maxRadius * scale)
    }
}

@Composable
private fun SquarePacer(progress: Float, modifier: Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    val marker = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val side = size.minDimension
        val inset = 12.dp.toPx()
        val box = Size(side - inset * 2, side - inset * 2)

        drawRect(
            color = outline,
            topLeft = Offset(inset, inset),
            size = box,
            style = Stroke(width = 4.dp.toPx()),
        )

        // Walk the perimeter once per full cycle, one side per phase.
        val perimeterProgress = progress * 4f
        val edge = perimeterProgress.toInt().coerceIn(0, 3)
        val along = perimeterProgress - edge
        val position = when (edge) {
            0 -> Offset(inset + box.width * along, inset)
            1 -> Offset(inset + box.width, inset + box.height * along)
            2 -> Offset(inset + box.width * (1 - along), inset + box.height)
            else -> Offset(inset, inset + box.height * (1 - along))
        }

        drawCircle(color = marker, radius = 14.dp.toPx(), center = position)
    }
}

/** The four phases of a breathing cycle, with the on-screen cue for each. */
private enum class BreathPhase(val labelRes: Int) {
    INHALE(R.string.breathing_in),
    HOLD_IN(R.string.breathing_hold),
    EXHALE(R.string.breathing_out),
    HOLD_OUT(R.string.breathing_pause),
}

private fun BreathingPattern.phaseAt(progress: Float): BreathPhase {
    val elapsed = progress * totalSeconds
    var boundary = inhale.toFloat()
    if (elapsed < boundary) return BreathPhase.INHALE
    boundary += holdIn
    if (elapsed < boundary) return BreathPhase.HOLD_IN
    boundary += exhale
    if (elapsed < boundary) return BreathPhase.EXHALE
    return BreathPhase.HOLD_OUT
}

/**
 * Circle radius as a fraction of maximum: grows across the inhale, shrinks across the
 * exhale. The skills master list specifies a 4-count in and an 8-count out.
 */
private fun BreathingPattern.circleScaleAt(progress: Float): Float {
    val elapsed = progress * totalSeconds
    val minScale = 0.35f
    return when {
        elapsed < inhale -> minScale + (1f - minScale) * (elapsed / inhale)
        elapsed < inhale + holdIn -> 1f
        elapsed < inhale + holdIn + exhale -> {
            val into = (elapsed - inhale - holdIn) / exhale
            1f - (1f - minScale) * into
        }
        else -> minScale
    }
}
