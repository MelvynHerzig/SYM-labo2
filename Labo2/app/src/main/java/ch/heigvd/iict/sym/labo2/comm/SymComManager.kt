package ch.heigvd.iict.sym.labo2.comm

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference
import java.util.Queue
import java.util.Timer
import java.util.TimerTask
import java.util.LinkedList


/**
 * Classe responsable de gérer la communication avec le thread de communication SymComThead
 * @param context Contexte de l'activité executant le manager pour récupérer l'état de la connexion.
 * @param debug Par défaut faux, si vrai notifie le communication event listener lorsque la requête
 * est mise en attente ou traitée.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class SymComManager(context: Context, private val debug: Boolean = false) {

    // Listener à notifier lors de réception d'une réponse
    private var mCommunicationEventListener: WeakReference<CommunicationEventListener>?

    // Context d'exécution pour récupération de l'état de la connexion
    private var mContext: WeakReference<Context>

    // Liste des requêtes en attente de traitement
    private var mQueue: Queue<Pair<SymComRequest, WeakReference<CommunicationEventListener>>>

    // Timer pour exécution récurrente des requêtes en cache.
    private var mTimer: Timer = Timer()

    init {
        mCommunicationEventListener = null
        mContext = WeakReference(context)
        mQueue   = LinkedList(listOf())

        // Toutes les 10s, si internet disponible, envoyer les requêtes en cache.
        mTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                while (mQueue.isNotEmpty() && checkForInternet()) {
                    val pair = mQueue.remove()
                    sendRequestDirect(pair.first, pair.second)
                }
            }
        }, 0, 10000)
    }

    /**
     * Set le listener qui sera utilisé pour les requêtes suivantes.
     */
    fun setCommunicationEventListener(communicationEventListener: CommunicationEventListener) {
        mCommunicationEventListener = WeakReference(communicationEventListener)
    }

    /**
     * Permet d'envoyer une requête au serveur
     */
    fun sendRequest( request: SymComRequest ) {

        if(mCommunicationEventListener != null && checkForInternet()) {
            // Démarrage transmission
            mCommunicationEventListener?.let { sendRequestDirect(request, it) }
        } else {
            mCommunicationEventListener?.let { responseIfDebug("Queued $request", it) }
            mCommunicationEventListener?.let { mQueue.add(Pair(request, it)) }
        }
    }

    /**
     * Créer un thread de communication sans vérifier la connexion.
     * Utilisé depuis le timer qui vérifie au préalable la connexion.
     */
    private fun sendRequestDirect( request: SymComRequest, weakListener: WeakReference<CommunicationEventListener>) {

        mCommunicationEventListener?.let { responseIfDebug("Sending $request", it )}
        SymComThread(weakListener, request).start()

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
     * Post a response on UI thread using the mCommunicationEventListener because this function can be
     * called from timer context
     */
    private fun responseIfDebug(message: String, listener: WeakReference<CommunicationEventListener>){
        if(debug) {
            Handler(Looper.getMainLooper()).post {
                listener.get()?.handleServerResponse(message.toByteArray())
            }
        }
    }

    /**
     * Quitte le symComManager et abandonne les requêtes cachées.
     */
    fun quit() {
        mTimer.cancel()
    }
}