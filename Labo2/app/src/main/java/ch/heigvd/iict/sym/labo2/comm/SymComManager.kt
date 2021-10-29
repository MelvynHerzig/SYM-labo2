/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.lang.ref.WeakReference

/**
 * Spécification du content type
 */
enum class ContentType(val value: String) {
    TEXT("text/plain"), JSON("application/json")
}

/**
 * Spécification de la méthode
 */
enum class RequestMethod(val value: String) {
    GET("GET"), POST("POST")
}

/**
 * Classe responsable de gérer la communication avec le thread de communication SymComThead
 * @param communicationEventListener Callback à utiliser quand une requête est traitée.
 */
open class SymComManager(communicationEventListener: CommunicationEventListener) {

    protected var mCommunicationEventListener: WeakReference<CommunicationEventListener>
    protected var mThread: SymComThread? = null

    init {
        mCommunicationEventListener = WeakReference(communicationEventListener)
        mThread = SymComThread()
        mThread?.start()
    }

    /**
     * Permet d'envoyer une requête au serveur
     */
    open fun sendRequest( url: String,
                          request: String,
                          contentType: ContentType,
                          requestMethod: RequestMethod
    ) {

        // Préparation du runnable
        val symComRunnable = SymComRunnable(url,
                                            request,
                                            contentType,
                                            requestMethod,
                                            mCommunicationEventListener)

        mThread?.mHandler?.post(symComRunnable)
    }

    /**
     * Utilisé pour temriner le thread
     */
    fun destroy() {
        mThread?.mHandler?.looper?.quit()
    }
}