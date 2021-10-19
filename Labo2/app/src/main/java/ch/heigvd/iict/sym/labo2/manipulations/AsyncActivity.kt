package ch.heigvd.iict.sym.labo2.manipulations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.lab.comm.SymComManager

/**
 * Activité implémentant le protocole de communication asynchrone.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class AsyncActivity : AppCompatActivity(){

    // Référence sur le champ input de l'utilisateur.
    private lateinit var userInput: EditText

    // Référence sur le bouton d'annulation.
    private lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    private lateinit var responseField: TextView

    /**
     * Binding des éléments graphiques
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)

        // Liaison des éléments graphiques.
        userInput  = findViewById(R.id.async_user_input)
        sendButton = findViewById(R.id.async_btn_send)
        responseField = findViewById(R.id.async_response_field)

        sendButton.setOnClickListener{
            responseField.setText("waiting for server response...")
        }
    }




}