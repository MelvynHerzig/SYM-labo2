package ch.heigvd.iict.sym.labo2.comm

/**
 * Interface utilisé pour gérer différents types de réponse du serveur
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
interface CommunicationEventListener {
    fun handleServerResponse(response : ByteArray)
}