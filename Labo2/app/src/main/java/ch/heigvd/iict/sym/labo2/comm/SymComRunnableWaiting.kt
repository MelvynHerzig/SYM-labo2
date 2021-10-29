/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.SystemClock
import android.util.Log

/**
 * Implémentation d'un runnable qui simule une attente de 10s du réseau et se remet dans le looper.
 */
class SymComRunnableWaiting (private val mHandler: Handler?) : Runnable{

    /**
     * Attente et self-post.
     */
    override fun run() {

        Log.d("OUE OUE", "I WAIT SIR")
        SystemClock.sleep(10000)
        Log.d("OUE OUE", "DONE WAITING")

        mHandler?.post(SymComRunnableWaiting(mHandler))
    }
}