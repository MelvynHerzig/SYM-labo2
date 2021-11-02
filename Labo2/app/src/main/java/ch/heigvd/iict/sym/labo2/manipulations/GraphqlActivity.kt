/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
package ch.heigvd.iict.sym.labo2.manipulations

import android.os.Bundle
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.R
import ch.heigvd.iict.sym.labo2.comm.SymComManager
import android.widget.ArrayAdapter
import ch.heigvd.iict.sym.labo2.comm.ContentType
import ch.heigvd.iict.sym.labo2.comm.RequestMethod
import ch.heigvd.iict.sym.labo2.comm.SymComRequest
import ch.heigvd.iict.sym.labo2.models.Author
import org.json.JSONObject


/**
 * Activité implémentant une communication avec graphQL.
 */
class GraphqlActivity : BaseActivity() {

    // Référence sur le champ input de l'utilisateur.
    protected lateinit var authorSpinner: Spinner

    // Référence sur le champ d'affichage de la réponse.
    protected lateinit var responseField: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphql)

        // Liaison des éléments graphiques.
        authorSpinner = findViewById(R.id.graphql_authors_spinner)
        responseField = findViewById(R.id.graphql_books_recyclerView)

        symComManager = SymComManager(this)

        fillSpinnerWithAuthors()

    }

    private fun fillSpinnerWithAuthors() {
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

                // Adaptation de la liste d'autheur
                val adapter: ArrayAdapter<Author> = ArrayAdapter(this@GraphqlActivity,
                                                                 android.R.layout.simple_list_item_1,
                                                                 authorsList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                authorSpinner.setAdapter(adapter)
            }
        })
        symComManager.sendRequest(SymComRequest("http://mobile.iict.ch/graphql", "{\"query\":\"{findAllAuthors{id, name}}\"}", ContentType.JSON, RequestMethod.POST))
    }
}