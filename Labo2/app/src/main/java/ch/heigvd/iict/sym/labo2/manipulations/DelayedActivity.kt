package ch.heigvd.iict.sym.labo2.manipulations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.iict.sym.labo2.R

/**
 * Activité implémentant le protocole de communication retardé.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class DelayedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delayed)
    }
}