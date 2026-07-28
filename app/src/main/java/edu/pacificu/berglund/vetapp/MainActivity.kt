package edu.pacificu.berglund.vetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import edu.pacificu.berglund.vetapp.ui.VetApp
import edu.pacificu.berglund.vetapp.ui.VetViewModel

/**
 * The app's only activity.
 *
 * It replaces a `BaseActivity`/`MainActivity` pair plus twelve fragments. The old
 * `MainActivity.onCreate` also built all 28 steps inline on every launch and committed
 * its first fragment from a background thread — which is where the
 * "can not perform this action after onSaveInstanceState" crashes came from on rotation.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: VetViewModel by viewModels {
        VetViewModel.Factory((application as VetApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            VetApp(
                viewModel = viewModel,
                onExit = { finish() },
            )
        }
    }
}
