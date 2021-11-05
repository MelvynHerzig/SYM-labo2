package ch.heigvd.iict.sym.labo2.comm

class SymComBytesRequest (url: String,
                          private val body : ByteArray,
                          contentType: ContentType,
                          requestMethod: RequestMethod)
    : SymComRequest(url, contentType, requestMethod) {

    override fun getBytesFromBody() : ByteArray {
        return body
    }
}