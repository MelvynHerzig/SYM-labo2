/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.manipulations

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.sym.labo2.comm.SymComManager

/**
 * Activité de base pour notre programme définissant les méthodes communes éléments communs.
 */
abstract class BaseActivity : AppCompatActivity() {

    // Référence sur le champ input de l'utilisateur.
    protected lateinit var userInput: EditText

    // Référence sur le bouton d'annulation.
    protected lateinit var sendButton: Button

    // Référence sur le champ d'affichage de la réponse.
    protected lateinit var responseField: TextView

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