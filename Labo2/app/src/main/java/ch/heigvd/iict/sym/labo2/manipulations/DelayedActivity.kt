/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.comm.ContentType
import ch.heigvd.iict.sym.labo2.comm.RequestMethod
import ch.heigvd.iict.sym.labo2.comm.SymComManager
import ch.heigvd.iict.sym.labo2.comm.SymComRequest

/**
 * Activité implémentant le protocole de communication retardé.
 */
class DelayedActivity : BaseActivity() {

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
            symComManager.sendRequest( SymComRequest("http://mobile.iict.ch/api/txt",
                                                     userInput.text.toString(),
                                                     ContentType.TEXT,
                                                     RequestMethod.POST))
        }
    }
}