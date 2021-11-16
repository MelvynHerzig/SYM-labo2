package ch.heigvd.iict.sym.labo2.comm

/**
 * Classe modélisant une requête transmissible au SymComManager ayant un body au format ByteArray
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class SymComBytesRequest (url: String,
                          private val body : ByteArray,
                          contentType: ContentType,
                          requestMethod: RequestMethod,
                          isCompressed: Boolean)
    : SymComRequest(url, contentType, requestMethod, isCompressed) {

    override fun getBytesFromBody() : ByteArray {
        return body
    }
}