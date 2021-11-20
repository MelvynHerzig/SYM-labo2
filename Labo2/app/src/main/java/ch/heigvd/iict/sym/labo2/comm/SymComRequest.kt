package ch.heigvd.iict.sym.labo2.comm

/**
 * Classe modélisant une requête transmissible au SymComManager
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
*/

abstract class SymComRequest (val url : String,
                              val contentType : ContentType,
                              val requestMethod: RequestMethod,
                              val isCompressed: Boolean) {

    /**
     * Récupère le body de la requête sous forme de ByteArray
     */
    abstract fun getBytesFromBody() : ByteArray

    // Identifiant de la requête, surtout utilisé pour le debug avec le SymcomManager
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

/**
 * Spécification du content type
 */
enum class ContentType(val value: String) {
    TEXT("text/plain"),
    JSON("application/json"),
    XML("application/xml"),
    PROTOBUF("application/protobuf");
}

/**
 * Spécification de la méthode
 */
enum class RequestMethod(val value: String) {
    GET("GET"),
    POST("POST")
}