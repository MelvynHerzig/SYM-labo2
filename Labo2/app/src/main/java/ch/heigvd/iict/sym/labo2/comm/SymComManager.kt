/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */

package ch.heigvd.iict.sym.labo2.comm

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import android.util.Log
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.lang.ref.WeakReference
import java.util.*


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
class SymComManager(context: Context, communicationEventListener: CommunicationEventListener) {

    // Listener à notifier lors de réception d'une réponse
    private var mCommunicationEventListener: WeakReference<CommunicationEventListener>

    // Context d'exécution pour récupération de l'état de la connexion
    private var mContext: WeakReference<Context>

    // Liste des requête en attente de traitement
    private var mQueue: MutableList<SymComRequest>

    // Timer pour exécution récurrente des requête en cache.
    private var mTimer: Timer = Timer()

    init {
        mCommunicationEventListener = WeakReference(communicationEventListener)
        mContext = WeakReference(context)
        mQueue   = mutableListOf()

        // Toutes les 10s, si internet disponible, envoyer les requêtes en cache.
        mTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                while (!mQueue.isEmpty() && checkForInternet()) {
                    sendRequest(mQueue.removeAt(0))
                }
            }
        }, 0, 10000)
    }

    /**
     * Permet d'envoyer une requête au serveur
     */
    fun sendRequest( request: SymComRequest ) {


        if(checkForInternet()) {
            // Démarrage transmission
            SymComThread(mCommunicationEventListener, request).start()
        } else {
            mQueue.add(request)
        }
    }

    /**
     * Vérifie la connectivité du réseau.
     * Source: https://www.geeksforgeeks.org/how-to-check-internet-connection-in-kotlin/
     * @return Vrai si le réseau est disponible sinon faux.
     */
    private fun checkForInternet(): Boolean {

        val context = mContext.get()

        // register activity with the connectivity manager service
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M or greater we need to use the
        // NetworkCapabilities to check what type of network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport, or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport or Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    /**
     * Quitte le symComManager et abandonne les requêtes cachées.
     */
    fun quit() {
        mTimer.cancel()
    }
}