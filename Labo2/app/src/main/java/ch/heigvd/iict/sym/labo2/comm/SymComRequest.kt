/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.lang.ref.WeakReference

/**
 * Classe modélisant une requête transmissible au SymComManager
 */
abstract class SymComRequest (val url : String,
                          val contentType : ContentType,
                          val requestMethod: RequestMethod) {

    abstract fun getBytesFromBody() : ByteArray


    // Identifiant de la requête
    private val id: Int = getId()

    /**
     * Retourne "Request <id>" en String.
     */
    override fun toString(): String {
        return "Request ${id}"
    }

    /**
     * Companion object pour attribution d'id unique.
     */
    private companion object{
        private var counter = 0

        fun getId(): Int {
           return counter++
        }
    }
}