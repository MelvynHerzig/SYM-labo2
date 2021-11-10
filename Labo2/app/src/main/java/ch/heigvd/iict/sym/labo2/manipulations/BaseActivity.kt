/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.sym.labo2.comm.SymComManager

/**
 * Activité de base pour notre programme définissant les méthodes communes éléments communs.
 */
abstract class BaseActivity : AppCompatActivity() {

    // Constantes référençant les URL sur les différentes API
    companion object {
        const val URL_API_TEXT = "http://mobile.iict.ch/api/txt"
        const val URL_API_JSON = "http://mobile.iict.ch/api/json"
        const val URL_API_XML = "http://mobile.iict.ch/api/xml"
        const val URL_API_PROTOBUF = "http://mobile.iict.ch/api/protobuf"
        const val URL_API_GRAPHQL = "http://mobile.iict.ch/graphql"
    }

    // Référence sur le gestionnaire de communication.
    protected lateinit var symComManager: SymComManager

    /**
     * Signale au SymComManager la fin de l'activité
     */
    override fun onDestroy() {
        super.onDestroy()
        symComManager.quit()
    }
}