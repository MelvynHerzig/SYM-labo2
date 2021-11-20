package ch.heigvd.iict.sym.labo2.comm

/**
 * Interface utilisée pour gérer les réponses du serveur
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
interface CommunicationEventListener {
    fun handleServerResponse(response : ByteArray)
}