/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.comm.*

/**
 * Activité implémentant le protocole de communication retardé.
 */
class DelayedActivity : BaseActivity() {

    // Référence sur le champ input de l'utilisateur.
    protected lateinit var userInput: EditText

    // Référence sur le bouton d'annulation.
    protected lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    protected lateinit var responseField: TextView

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

        symComManager = SymComManager(this, object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {

                if (responseField.text == getString(R.string.str_waiting_server)) {
                    responseField.text = ""
                }

                if (responseField.text == "") {
                    responseField.text = response
                } else {
                    "${responseField.text}\n------\n$response".also { responseField.text = it }
                }
            }
            override fun handleServerResponse(response: ByteArray) {
                throw Exception("Impossible to read ByteArray")
            }
        })

        sendButton.setOnClickListener {

            responseField.text = getString(R.string.str_waiting_server)
            symComManager.sendRequest(
                SymComStringRequest(
                    "http://mobile.iict.ch/api/txt",
                    userInput.text.toString(),
                    ContentType.TEXT,
                    RequestMethod.POST
                )
            )
        }
    }
}