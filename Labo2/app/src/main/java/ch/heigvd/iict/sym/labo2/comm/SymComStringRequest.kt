package ch.heigvd.iict.sym.labo2.comm

import java.nio.charset.StandardCharsets

class SymComStringRequest (url : String,
                           private val body : String,
                           contentType  : ContentType,
                           requestMethod: RequestMethod)
    : SymComRequest(url, contentType, requestMethod) {

    override fun getBytesFromBody() : ByteArray {
        return body.toByteArray(StandardCharsets.UTF_8)
    }
}