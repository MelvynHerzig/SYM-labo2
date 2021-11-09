/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.iict.sym.labo2.comm.*
import ch.heigvd.iict.sym.labo2.manipulations.adapter.StringListAdapter
import ch.heigvd.iict.sym.labo2.models.Author
import org.json.JSONObject


/**
 * Activité implémentant une communication avec graphQL.
 * Affiche une liste d'autheur dans un spinner et lorsque un
 * autheur est sélectionné, ses oeuvres sont affichées dans un recyclerView.
 */
class GraphqlActivity : BaseActivity() {

    // Référence sur le champ input de l'utilisateur (spinner des autheurs).
    protected lateinit var authorSpinner: Spinner

    // Référence sur le champ d'affichage des livres (RecyclerView).
    protected lateinit var responseField: RecyclerView

    /**
     * À la création de l'activité.
     * @param savedInstanceState Bundle de sauvegarde (non utilisé)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphql)

        // Liaison des éléments graphiques.
        authorSpinner = findViewById(R.id.graphql_authors_spinner)
        responseField = findViewById(R.id.graphql_books_recyclerView)

        // Adaptateur vite pour éviter: E/RecyclerView: No adapter attached; skipping layout
        val bookList: MutableList<String> = mutableListOf()
        val adapter = StringListAdapter(bookList)
        responseField.adapter = adapter
        responseField.layoutManager = LinearLayoutManager(this@GraphqlActivity)

        // Communication
        symComManager = SymComManager(this)

        // Peuplement
        fillSpinnerWithAuthors()

        // Quand un autheur est sélectionné
        authorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Peuplement de la recyclerView
                fillAuthorBooks(authorSpinner.selectedItem as Author)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { /* Ne fait rien */ }
        }

    }

    /**
     * Remplit le spinner des autheurs.
     */
    private fun fillSpinnerWithAuthors() {
        // Notification du démarrage
        Toast.makeText(applicationContext, getString(R.string.str_authors_loading), Toast.LENGTH_SHORT).show()

        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {

                // Récupération de la réponse sous forme d'un tableau d'autheurs
                val allAuthors = JSONObject(response).getJSONObject("data").getJSONArray("findAllAuthors")
                val authorsList: MutableList<Author> = mutableListOf()

                // Transformation des autheurs JSON en autheur Kotlin
                for (i in 0 until allAuthors.length()) {
                    val o = allAuthors.getJSONObject(i)
                    authorsList.add(
                        Author(
                            o.getInt("id"),
                            o.getString("name")
                        )
                    )
                }

                // Adaptation de la liste d'autheurs
                val adapter: ArrayAdapter<Author> = ArrayAdapter(this@GraphqlActivity,
                                                                 android.R.layout.simple_list_item_1,
                                                                 authorsList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                authorSpinner.setAdapter(adapter)

                // Notification opération terminée.
                Toast.makeText(applicationContext, getString(R.string.str_authors_loaded), Toast.LENGTH_SHORT).show()
            }
            override fun handleServerResponse(response: ByteArray) {
                throw Exception("Impossible to read ByteArray")
            }
        })
        // Envoie de la demande des autheurs
        symComManager.sendRequest(SymComStringRequest("http://mobile.iict.ch/graphql", "{\"query\":\"{findAllAuthors{id, name}}\"}", ContentType.JSON, RequestMethod.POST))
    }

    /**
     * Remplit la liste des livres pour un autheur donné.
     * @param author Autheur à rechercher les livres.
     */
    private fun fillAuthorBooks(author: Author) {

        symComManager.setCommunicationEventListener(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {

                // Récupération de la réponse sous forme d'un tableau de livres
                val booksFromAuthor = JSONObject(response).getJSONObject("data").getJSONObject("findAuthorById").getJSONArray("books")
                val bookList: MutableList<String> = mutableListOf()

                // Transformation des livres JSON en livres Kotlin
                for (i in 0 until booksFromAuthor.length()) {
                    val o = booksFromAuthor.getJSONObject(i)
                    bookList.add(o.getString("title"))
                }

                // Adaptation de la liste de livres
                val adapter = StringListAdapter(bookList)
                responseField.adapter = adapter
                responseField.layoutManager = LinearLayoutManager(this@GraphqlActivity)
            }
            override fun handleServerResponse(response: ByteArray) {
                throw Exception("Impossible to read ByteArray")
            }
        })

        // Envoie de la demande des livres
        symComManager.sendRequest(SymComStringRequest("http://mobile.iict.ch/graphql", "{\"query\": \"{findAuthorById(id: ${author.id}){books{title}}}\"}", ContentType.JSON, RequestMethod.POST))
    }
}