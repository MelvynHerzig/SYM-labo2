/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

/**
 * Classe modélisant une requête transmissible au SymComManager
 */
data class SymComRequest (val url          : String,
                          val body         : String,
                          val contentType  : ContentType,
                          val requestMethod: RequestMethod) {}