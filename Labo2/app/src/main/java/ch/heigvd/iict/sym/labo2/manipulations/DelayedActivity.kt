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

    // Référence sur le bouton d'envoi.
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

        symComManager = SymComManager(this, true)
        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {

                if (responseField.text == "") {
                    responseField.text = response
                } else {
                    "$response\n------\n${responseField.text}".also { responseField.text = it }
                }
            }
        })

        sendButton.setOnClickListener {

            if(userInput.text.toString() == "") {
                return@setOnClickListener
            }

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