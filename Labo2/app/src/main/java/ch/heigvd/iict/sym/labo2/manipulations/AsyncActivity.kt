/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.comm.ContentType
import ch.heigvd.iict.sym.labo2.comm.RequestMethod
import ch.heigvd.iict.sym.labo2.comm.SymComManager
import ch.heigvd.iict.sym.labo2.comm.SymComRequest

/**
 * Activité implémentant le protocole de communication asynchrone.
 */
class AsyncActivity : AppCompatActivity() {

    // Référence sur le champ input de l'utilisateur.
    private lateinit var userInput: EditText

    // Référence sur le bouton d'annulation.
    private lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    private lateinit var responseField: TextView

    // Référence sur le gestionnaire de communication.
    private lateinit var symComManager: SymComManager

    /**
     * Attachement des éléments graphiques
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)

        // Liaison des éléments graphiques.
        userInput = findViewById(R.id.async_user_input)
        sendButton = findViewById(R.id.async_btn_send)
        responseField = findViewById(R.id.async_response_field)

        symComManager = SymComManager(this, object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                responseField.text = response
            }
        })

        sendButton.setOnClickListener {

            responseField.text = getString(R.string.str_waiting_server)
            symComManager.sendRequest( SymComRequest("http://mobile.iict.ch/api/txt",
                                                     userInput.text.toString(),
                                                     ContentType.TEXT,
                                                     RequestMethod.POST))
        }
    }

    /**
     * Signale au SymComManager la fin de l'activité
     */
    override fun onDestroy() {
        super.onDestroy()
        symComManager.quit()
    }
}