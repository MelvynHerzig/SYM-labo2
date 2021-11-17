package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.widget.*
import ch.heigvd.iict.sym.labo2.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.models.Person
import ch.heigvd.iict.sym.labo2.models.Phone
import ch.heigvd.iict.sym.labo2.comm.*

/**
 * Activité implémentant le protocole de communication sérialisé.
 * Les protocoles pouvant être utiliser sont JSON, XML et Protocol Buffer
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
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

        sendButton.setOnClickListener {

            val person = createPersonFromForm()

            if (person != null) {
                responseField.text = getString(R.string.str_waiting_server)

                when (requestTypeSpinner.selectedItem) {
                    ContentType.JSON -> sendJSON(person)
                    ContentType.PROTOBUF -> sendProtobuf(person)
                    ContentType.XML -> sendXML(person)
                    ContentType.TEXT -> sendText(person)
                }
            } else {
                Toast.makeText(baseContext, "Des champs n'ont pas été rempli", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Constuit un objet Person avec les valeurs des champs textes
     */
    private fun createPersonFromForm(): Person? {

        val phones = mutableListOf<Phone>()

        if (nameInput.text.isEmpty() || firstnameInput.text.isEmpty() || middlenameInput.text.isEmpty()) {
            return null
        }

        if (!phonenumberInputHome.text.isEmpty()) {
            phones.add(Phone(phonenumberInputHome.text.toString(), Phone.Type.HOME))
        }

        if (!phonenumberInputMobile.text.isEmpty()) {
            phones.add(Phone(phonenumberInputMobile.text.toString(), Phone.Type.MOBILE))
        }

        if (!phonenumberInputWork.text.isEmpty()) {
            phones.add(Phone(phonenumberInputWork.text.toString(), Phone.Type.WORK))
        }

        return Person(
            nameInput.text.toString(),
            firstnameInput.text.toString(),
            middlenameInput.text.toString(),
            phones
        )
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec le Protocol Buffer
     */
    private fun sendProtobuf(person: Person) {

        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = Person.parsingDirectoryByteArrayData(response)
            }
        })

        symComManager.sendRequest(
            SymComBytesRequest(
                URL_API_PROTOBUF,
                Person.creatingByteArrayForProtobufData(person),
                ContentType.PROTOBUF,
                RequestMethod.POST,
                false
            )
        )
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec JSON
     */
    private fun sendJSON(person: Person) {

        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = Person.fromJson(String(response)).toString()
            }
        })

        symComManager.sendRequest(
            SymComStringRequest(
                URL_API_JSON,
                person.toJson(),
                ContentType.JSON,
                RequestMethod.POST,
                false
            )
        )
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec XML
     */
    private fun sendXML(person: Person) {
        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = Person.fromXML(String(response)).toString()
            }
        })


        symComManager.sendRequest(
            SymComStringRequest(
                URL_API_XML,
                person.toXml(),
                ContentType.XML,
                RequestMethod.POST,
                false
            )
        )
    }

    /**
     * Construit une requête avec la personne donnée
     * Puis envoie cette dernière au serveur avec du Texte
     */
    private fun sendText(person: Person) {
        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: ByteArray) {
                responseField.text = String(response)
            }
        })

        symComManager.sendRequest(
            SymComStringRequest(
                URL_API_TEXT,
                person.toString(),
                ContentType.TEXT,
                RequestMethod.POST,
                false
            )
        )
    }
}