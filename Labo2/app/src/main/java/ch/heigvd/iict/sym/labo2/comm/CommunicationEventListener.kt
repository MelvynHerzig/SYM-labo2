/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.lab.comm

/**
 * Interface utilisé pour gérer différents types de réponse du serveur
 */
interface CommunicationEventListener {
    fun handleServerResponse(response : String)
    fun handleServerResponse(response : ByteArray)
}