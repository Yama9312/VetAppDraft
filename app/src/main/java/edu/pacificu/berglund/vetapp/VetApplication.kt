package edu.pacificu.berglund.vetapp

import android.app.Application
import edu.pacificu.berglund.vetapp.data.VetDatabase
import edu.pacificu.berglund.vetapp.data.VetRepository

/**
 * Manual dependency wiring.
 *
 * The app has exactly one repository and one database. A DI framework would be more
 * machinery than this earns, but the graph is still built in one place rather than every
 * screen reaching for `VetDatabase.getInstance(context)` on its own — which is what the
 * fragments used to do.
 */
class VetApplication : Application() {

    val repository: VetRepository by lazy {
        VetRepository(VetDatabase.getInstance(this).vetDao())
    }
}
