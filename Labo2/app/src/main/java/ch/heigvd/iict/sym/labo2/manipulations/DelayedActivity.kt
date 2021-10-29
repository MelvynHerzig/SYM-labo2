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
import ch.heigvd.iict.sym.labo2.comm.SymComManagerDelayed

/**
 * Activité implémentant le protocole de communication retardé.
 */
class DelayedActivity : AppCompatActivity() {

    // Référence sur le champ input de l'utilisateur.
    private lateinit var userInput: EditText

    // Référence sur le bouton d'annulation.
    private lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    private lateinit var responseField: TextView

    // Référence sur le gestionnaire de communication.
    private lateinit var symComManager: SymComManager

    /**
     * Binding des éléments graphiques
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delayed)


        // Liaison des éléments graphiques.
        userInput = findViewById(R.id.delayed_user_input)
        sendButton = findViewById(R.id.delayed_btn_send)
        responseField = findViewById(R.id.delayed_response_field)

        symComManager = SymComManagerDelayed(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {

                if (responseField.text == getString(R.string.str_waiting_server)) {
                    responseField.text = ""
                }

                if(responseField.text == "") {
                    responseField.text = response
                }
                else {
                    "${responseField.text}\n------\n$response".also { responseField.text = it }
                }
            }
        })

        sendButton.setOnClickListener {
            responseField.text = getString(R.string.str_waiting_server)

            symComManager.sendRequest(
                "http://mobile.iict.ch/api/txt",
                userInput.text.toString(),
                ContentType.TEXT,
                RequestMethod.POST
            )
        }
    }

    /**
     * Notifie au symComManager qu'il peut clore le thread de communication
     */
    override fun onDestroy() {
        super.onDestroy()
        symComManager.destroy()
    }
}