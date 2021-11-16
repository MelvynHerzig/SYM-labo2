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

/**
 * Activité implémentant le protocole de communication compressé.
 */
class CompressedActivity : BaseActivity() {

    // Référence sur le champ input de l'utilisateur.
    private lateinit var userInput: EditText

    // Référence sur le bouton d'envoi.
    private lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    private lateinit var responseField: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compressed)

        // Liaison des éléments graphiques.
        userInput = findViewById(R.id.compressed_user_input)
        sendButton = findViewById(R.id.compressed_btn_send)
        responseField = findViewById(R.id.compressed_response_field)

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

            symComManager.sendRequest(
                SymComStringRequest(
                    URL_API_TEXT,
                    userInput.text.toString(),
                    ContentType.TEXT,
                    RequestMethod.POST,
                    true
                )
            )
        }
    }
}