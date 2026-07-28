package edu.pacificu.berglund.vetapp.ui.components

import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Playback state for a step's guided narration.
 *
 * The player is torn down when the composable leaves and paused when the app is
 * backgrounded. The old screen leaked it in the other direction: it released and
 * recreated a `MediaPlayer` on every tap of the play button, so tapping twice restarted
 * the audio from zero with no way to pause.
 */
class AudioController internal constructor(
    private val play: () -> Unit,
    private val pause: () -> Unit,
    private val restart: () -> Unit,
    val isPlaying: Boolean,
) {
    fun toggle() = if (isPlaying) pause() else play()

    /**
     * Start again from the beginning. The wireframe calls for this explicitly: "this
     * needs a repeat button so they can do it over".
     */
    fun repeat() = restart()
}

@Composable
fun rememberAudioController(@RawRes res: Int): AudioController {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isPlaying by remember(res) { mutableStateOf(false) }

    val player = remember(res) { MediaPlayer.create(context, res) }

    DisposableEffect(player, lifecycleOwner) {
        val completion = MediaPlayer.OnCompletionListener { isPlaying = false }
        player?.setOnCompletionListener(completion)

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (player?.isPlaying == true) player.pause()
                isPlaying = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player?.setOnCompletionListener(null)
            player?.release()
        }
    }

    return AudioController(
        play = {
            player?.start()
            isPlaying = true
        },
        pause = {
            player?.pause()
            isPlaying = false
        },
        restart = {
            player?.seekTo(0)
            player?.start()
            isPlaying = true
        },
        isPlaying = isPlaying,
    )
}
