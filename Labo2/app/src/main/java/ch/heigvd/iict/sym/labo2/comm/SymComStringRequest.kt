package ch.heigvd.iict.sym.labo2.comm

import java.nio.charset.StandardCharsets

/**
 * Classe modélisant une requête transmissible au SymComManager ayant un body au format String
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class SymComStringRequest (url : String,
                           private val body : String,
                           contentType  : ContentType,
                           requestMethod: RequestMethod,
                           isCompressed: Boolean)
    : SymComRequest(url, contentType, requestMethod, isCompressed) {

    /**
     * Retourne un tableau de bytes en fonction des données de la requête
     */
    override fun getBytesFromBody() : ByteArray {
        return body.toByteArray(StandardCharsets.UTF_8)
    }
}