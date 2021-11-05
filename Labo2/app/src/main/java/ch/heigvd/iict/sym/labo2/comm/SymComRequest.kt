/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

/**
 * Classe modélisant une requête transmissible au SymComManager
 */
abstract class SymComRequest (val url : String,
                          val contentType : ContentType,
                          val requestMethod: RequestMethod) {

    abstract fun getBytesFromBody() : ByteArray
}