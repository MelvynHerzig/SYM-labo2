/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener

/**
 * Manager qui transmet au thread de communication une tâche d'attente redondante.
 */
class SymComManagerDelayed(communicationEventListener: CommunicationEventListener)
    : SymComManager(communicationEventListener) {

    /**
     * Vrai tant que la première requête n'a pas été envoyée.
     */
    private var mFirstRequest: Boolean = true

    /**
     * Permet d'envoyer une requête au serveur
     */
    override fun sendRequest( url: String,
                              request: String,
                              contentType: ContentType,
                              requestMethod: RequestMethod
    ) {

        // Si première requête, simultation des indisponnibilités.
        if (mFirstRequest) {
            mThread?.mHandler?.post(SymComRunnableWaiting(mThread?.mHandler))
            mFirstRequest = false
        }

        // Préparation du runnable
        val symComRunnable = SymComRunnable(url,
            request,
            contentType,
            requestMethod,
            mCommunicationEventListener)

        mThread?.mHandler?.post(symComRunnable)
    }
}