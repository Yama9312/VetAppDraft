package edu.pacificu.berglund.vetapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import edu.pacificu.berglund.vetapp.domain.Branch
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Guards the palette against a readability regression.
 *
 * The previous version shipped emergency phone numbers in `@android:color/holo_blue_dark`
 * on cream — about 2.6:1, well under AA — and was entirely unreadable in dark mode. This
 * test fails the build if any foreground/background pair in any of the six branches, in
 * either mode, drops below WCAG AA.
 */
class ContrastTest {

    private fun luminance(color: Color): Double {
        fun channel(c: Float): Double {
            val v = c.toDouble()
            return if (v <= 0.03928) v / 12.92 else ((v + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(color.red) +
            0.7152 * channel(color.green) +
            0.0722 * channel(color.blue)
    }

    private fun ratio(a: Color, b: Color): Double {
        val la = luminance(a)
        val lb = luminance(b)
        return (max(la, lb) + 0.05) / (min(la, lb) + 0.05)
    }

    private fun ColorScheme.pairs(): List<Triple<String, Color, Color>> = listOf(
        Triple("onBackground/background", onBackground, background),
        Triple("onSurface/surface", onSurface, surface),
        Triple("onSurfaceVariant/surfaceVariant", onSurfaceVariant, surfaceVariant),
        Triple("onPrimary/primary", onPrimary, primary),
        Triple("onPrimaryContainer/primaryContainer", onPrimaryContainer, primaryContainer),
        Triple("onSecondary/secondary", onSecondary, secondary),
        Triple("onSecondaryContainer/secondaryContainer", onSecondaryContainer, secondaryContainer),
        Triple("onError/error", onError, error),
        Triple("onErrorContainer/errorContainer", onErrorContainer, errorContainer),
    )

    @Test
    fun `every branch meets WCAG AA in both modes`() {
        val failures = mutableListOf<String>()

        Branch.entries.forEach { branch ->
            listOf(false, true).forEach { dark ->
                val mode = if (dark) "dark" else "light"
                colorSchemeFor(branch, dark).pairs().forEach { (name, fg, bg) ->
                    val r = ratio(fg, bg)
                    if (r < AA) {
                        failures += "%s/%s %s = %.2f:1 (needs %.1f:1)"
                            .format(branch.name, mode, name, r, AA)
                    }
                }
            }
        }

        assertTrue(
            "Contrast failures:\n" + failures.joinToString("\n"),
            failures.isEmpty(),
        )
    }

    /** Body copy sits on surface/background, so hold those to the stricter AAA bar. */
    @Test
    fun `body text reaches WCAG AAA in both modes`() {
        val failures = mutableListOf<String>()

        Branch.entries.forEach { branch ->
            listOf(false, true).forEach { dark ->
                val scheme = colorSchemeFor(branch, dark)
                val mode = if (dark) "dark" else "light"
                listOf(
                    "onBackground/background" to (scheme.onBackground to scheme.background),
                    "onSurface/surface" to (scheme.onSurface to scheme.surface),
                ).forEach { (name, pair) ->
                    val r = ratio(pair.first, pair.second)
                    if (r < AAA) {
                        failures += "%s/%s %s = %.2f:1 (needs %.1f:1)"
                            .format(branch.name, mode, name, r, AAA)
                    }
                }
            }
        }

        assertTrue(
            "Body-text contrast failures:\n" + failures.joinToString("\n"),
            failures.isEmpty(),
        )
    }

    private companion object {
        const val AA = 4.5
        const val AAA = 7.0
    }
}
