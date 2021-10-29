/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

// Clé permettant de retrouver l'exécutable dans un bundle.
const val RUN_KEY = "KeyRun"

/**
 * Spécification du content type
 */
enum class MessageWhat(val value: String) {
    TASK("text/plain"), EXIT("application/json")
}

/**
 * Classe implémentant le thread de communication http avec un serveur.
 * Pour des raisons de simplicité, le thread communique avec le même serveur et avec le même contenu.
 */
class SymComThread() : Thread() {

    /**
     * Handler associé au thread.
     */
    var mHandler: Handler? = null
    get() = field
    private set

    var mLooper: Looper? = null
    get() = field
    private set

    /**
     * Handler permettant le transfert de message au SymComThread
     */
    private class SymComThreadHandler(symComThread: SymComThread) : Handler(Looper.myLooper()!!) {

        // Référence sur le thread.
        private val mThread: WeakReference<SymComThread>

        override fun handleMessage(msg: Message) {
            post(msg.data.get(RUN_KEY) as Runnable)
        }

        init {
            mThread = WeakReference(symComThread)
        }
    }

    /**
     * Exécution du thread sur le looper.
     */
    override fun run() {

        Looper.prepare()
        mLooper = Looper.myLooper()

        mHandler = SymComThreadHandler(this)

        Looper.loop()
    }
}


