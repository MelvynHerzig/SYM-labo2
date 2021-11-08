/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.widget.*
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.models.Person
import ch.heigvd.iict.sym.labo2.models.Phone
import android.widget.ArrayAdapter
import ch.heigvd.iict.sym.labo2.comm.*
import ch.heigvd.iict.sym.labo2.protobuf.DirectoryOuterClass

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
    protected lateinit var responseField: TextView

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

        requestTypeSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ContentType.values()
        )

        symComManager = SymComManager(this)
        symComManager.setCommunicationEventListener( object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = Person.parsingDirectoryByteArrayData(response)
            }
            override fun handleServerResponse(response: String) {
                // TODO: gérer les différentes façon de recevoir
                responseField.text = response
            }
        })

        sendButton.setOnClickListener {
            responseField.text = getString(R.string.str_waiting_server)
            val person = createPersonFromForm()

            when (requestTypeSpinner.selectedItem) {
                ContentType.JSON -> sendJSON(person)
                ContentType.PROTOBUF -> sendProtobuf(person)
                ContentType.XML -> sendXML(person)
                ContentType.TEXT -> sendText(person)
            }
        }
    }

    /**
     * Constuit un objet Person avec les valeurs des champs textes
     */
    private fun createPersonFromForm() : Person {
        return Person(nameInput.text.toString(),
            firstnameInput.text.toString(),
            middlenameInput.text.toString(),
            mutableListOf(Phone(phonenumberInputHome.text.toString(), Phone.Type.HOME),
                Phone(phonenumberInputHome.text.toString(), Phone.Type.MOBILE),
                Phone(phonenumberInputHome.text.toString(), Phone.Type.WORK)))
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec le Protocol Buffer
     */
    private fun sendProtobuf(person : Person) {
        symComManager.sendRequest( SymComBytesRequest("http://mobile.iict.ch/api/protobuf",
            Person.creatingByteArrayForProtobufData(person),
            ContentType.PROTOBUF,
            RequestMethod.POST))
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec JSON
     */
    private fun sendJSON(person : Person) {
        symComManager.sendRequest( SymComStringRequest("http://mobile.iict.ch/api/protobuf",
            person.toString(), //TODO
            ContentType.JSON,
            RequestMethod.POST))
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec XML
     */
    private fun sendXML(person : Person) {
        symComManager.sendRequest( SymComStringRequest("http://mobile.iict.ch/api/protobuf",
            person.toString(), //TODO
            ContentType.XML,
            RequestMethod.POST))
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec du Texte
     */
    private fun sendText(person : Person) {
        symComManager.sendRequest( SymComStringRequest("http://mobile.iict.ch/api/protobuf",
            person.toString(),
            ContentType.TEXT,
            RequestMethod.POST))
    }
}