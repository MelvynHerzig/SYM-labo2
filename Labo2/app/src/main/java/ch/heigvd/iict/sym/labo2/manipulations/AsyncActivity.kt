package ch.heigvd.iict.sym.labo2.manipulations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R

/**
 * Activité implémentant le protocole de communication asynchrone.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class AsyncActivity : AppCompatActivity(), CommunicationEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)
    }

    override fun handleServerResponse(response: String) {

    }
}