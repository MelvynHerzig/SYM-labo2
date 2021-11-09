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
import ch.heigvd.iict.sym.labo2.comm.ContentType
import ch.heigvd.iict.sym.labo2.comm.RequestMethod
import ch.heigvd.iict.sym.labo2.comm.SymComManager
import ch.heigvd.iict.sym.labo2.comm.SymComStringRequest
import ch.heigvd.iict.sym.labo2.models.Person
import java.lang.Error

/**
 * Activité implémentant le protocole de communication asynchrone.
 */
class AsyncActivity : BaseActivity() {

    // Référence sur le champ input de l'utilisateur.
    protected lateinit var userInput: EditText

    // Référence sur le bouton d'envoi.
    protected lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    protected lateinit var responseField: TextView

    /**
     * À la création de l'activité.
     * Attachement des éléments graphiques
     * Création de listener sur les réponses du serveur
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)

        // Liaison des éléments graphiques.
        userInput = findViewById(R.id.async_user_input)
        sendButton = findViewById(R.id.async_btn_send)
        responseField = findViewById(R.id.async_response_field)

        symComManager = SymComManager(this)
        symComManager.setCommunicationEventListener( object : CommunicationEventListener {
            
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = String(response)
            }
        })

        sendButton.setOnClickListener {
            if(userInput.text.toString() == "") {
                return@setOnClickListener
            }
            responseField.text = getString(R.string.str_waiting_server)
            symComManager.sendRequest( SymComStringRequest("http://mobile.iict.ch/api/txt",
                    userInput.text.toString(),
                    ContentType.TEXT,
                    RequestMethod.POST))
        }
    }
}