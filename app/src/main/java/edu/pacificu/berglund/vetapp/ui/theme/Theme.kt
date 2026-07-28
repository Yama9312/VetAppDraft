package edu.pacificu.berglund.vetapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import edu.pacificu.berglund.vetapp.domain.Branch

/**
 * The app theme.
 *
 * Selecting a branch palette is one scheme swap. That is the whole reason this is
 * Compose: the previous version needed six theme overlays plus an activity recreation to
 * do the same thing, so instead it painted a `GradientDrawable` directly onto the window
 * decor from three copy-pasted 30-line `switch` statements — which is what made the app
 * unreadable in dark mode.
 *
 * Dynamic colour is intentionally **not** used. The branch palette is a documented
 * requirement and doubles as a mitigation: the project notes that some colours may remind
 * veterans of service in an unwelcome way, so the choice has to stay the veteran's, not
 * the wallpaper's.
 */
@Composable
fun VetTheme(
    branch: Branch = Branch.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = colorSchemeFor(branch, darkTheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VetTypography,
        content = content,
    )
}
