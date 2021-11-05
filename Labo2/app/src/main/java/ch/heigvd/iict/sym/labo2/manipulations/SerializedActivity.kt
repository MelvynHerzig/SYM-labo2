/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.comm.ContentType
import ch.heigvd.iict.sym.labo2.comm.RequestMethod
import ch.heigvd.iict.sym.labo2.comm.SymComManager
import ch.heigvd.iict.sym.labo2.comm.SymComBytesRequest
import ch.heigvd.iict.sym.labo2.models.Person
import ch.heigvd.iict.sym.labo2.models.Phone

/**
 * Activité implémentant le protocole de communication sérialisé.
 * Les protocoles pouvant être utiliser sont JSON, XML et Protocol Buffer
 */
class SerializedActivity : BaseActivity() {

    // Référence sur le champ input du nom.
    private lateinit var nameInput: EditText

    // Référence sur le champ input du prénom.
    private lateinit var firstnameInput: EditText

    // Référence sur le champ input du 2ème prénom.
    private lateinit var middlenameInput: EditText

    // Référence sur le champ input du numéro de téléphone.
    private lateinit var phonenumberInputHome: EditText

    // Référence sur le champ input du numéro de téléphone.
    private lateinit var phonenumberInputMobile: EditText

    // Référence sur le champ input du numéro de téléphone.
    private lateinit var phonenumberInputWork: EditText

    // Référence sur le spinner correspondant au type de sérialisation
    private lateinit var requestTypeSpinner: Spinner

    // Référence sur le bouton d'annulation.
    private lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    private lateinit var responseField: TextView

    /**
     * À la création de l'activité.
     * Attachement des éléments graphiques
     * Création de listener sur les réponses du serveur
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serialized)

        nameInput = findViewById(R.id.serialize_name_input)
        firstnameInput = findViewById(R.id.serialize_firstname_input)
        middlenameInput = findViewById(R.id.serialize_middlename_input)
        phonenumberInputHome = findViewById(R.id.serialize_phone_number_input_home)
        phonenumberInputMobile = findViewById(R.id.serialize_phone_number_input_mobile)
        phonenumberInputWork = findViewById(R.id.serialize_phone_number_input_work)
        requestTypeSpinner = findViewById(R.id.serialize_spinner_request_type)
        sendButton = findViewById(R.id.serialize_btn_send)
        responseField = findViewById(R.id.serialize_response_field)

        // Adaptation de la liste d'autheurs
        /*val adapter: ArrayAdapter<Author> = ArrayAdapter(this@SerializedActivity,
            android.R.layout.simple_list_item_1,
            authorsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        authorSpinner.setAdapter(adapter)*/

        symComManager = SymComManager(this)
        symComManager.setCommunicationEventListener( object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = Person.parsingProtobufByteArrayData(response)
            }
            override fun handleServerResponse(response: String) {
                responseField.text = response
            }
        })

        sendButton.setOnClickListener {

            sendProtobuf()

            /*when (requestTypeSpinner.selectedItem) {
                ContentType.JSON -> sendProtobuf()
                ContentType.PROTOBUF -> sendProtobuf()

            }*/
        }
    }

    /**
     * Construit une requête en fonction des valeurs du formulaire
     * Puis envoie cette dernière au serveur avec le Protocol Buffer
     */
    private fun sendProtobuf() {
        val phoneHome = Phone(phonenumberInputHome.text.toString(), Phone.Type.HOME)
        val phoneMobile = Phone(phonenumberInputHome.text.toString(), Phone.Type.MOBILE)
        val phoneWork = Phone(phonenumberInputHome.text.toString(), Phone.Type.WORK)

        val person = Person(nameInput.text.toString(),
            firstnameInput.text.toString(),
            middlenameInput.text.toString(),
            mutableListOf(phoneHome, phoneMobile, phoneWork))

        symComManager.sendRequest( SymComBytesRequest("http://mobile.iict.ch/api/protobuf",
            person.creatingByteArrayForProtobufData(),
            ContentType.PROTOBUF,
            RequestMethod.POST))
    }
}