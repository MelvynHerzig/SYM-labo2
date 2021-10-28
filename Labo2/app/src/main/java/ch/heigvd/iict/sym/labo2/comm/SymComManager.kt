package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

// Spécification du content type
enum class ContentType(val value: String) {
    TEXT("text/plain"), JSON("application/json")
}

// Spécification de la méthode
enum class RequestMethod(val value: String) {
    GET("GET"), POST("POST")
}

/**
 * Classe responsable d'effectuer des requêtes asynchrones au serveur.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class SymComManager(var mCommunicationEventListener: CommunicationEventListener) {

    /**
     * Permet d'envoyer une requête au serveur
     */
    fun sendRequest( url: String,
                     request: String,
                     contentType: ContentType,
                     requestMethod: RequestMethod
    ) {

        // Préparation du runnable
        var symComRunnable = SymComRunnable(url,
                                            request,
                                            contentType,
                                            requestMethod,
                                            mCommunicationEventListener)

        // Lancement du thread
        Thread(symComRunnable).start()
    }

    /**
     * Classe interne sans référence sur la classe mère pour l'exécution de communication.
     * @param url Serveur sur lequel se connecter.
     * @param request Contenu.
     * @param contentType Type de contenu (en header http)
     * @param requestMethod type de requête http.
     * @param listener Listener à notifier une fois la réponse reçue.
     */
    private class SymComRunnable(url          : String,
                                 request      : String,
                                 contentType  : ContentType,
                                 requestMethod: RequestMethod,
                                 listener     : CommunicationEventListener) : Runnable {

        private var mUrl           : String = url
        private var mRequest       : String = request
        private var mContentType   : ContentType = contentType
        private var mRequestMethod : RequestMethod = requestMethod
        private var mListener: WeakReference<CommunicationEventListener> = WeakReference(listener)

        override fun run() {

            val handler = Handler(Looper.getMainLooper())
            val connection = URL(mUrl).openConnection() as HttpURLConnection

            connection.connectTimeout = 300000

            try {
                val postData = mRequest.toByteArray(StandardCharsets.UTF_8)

                connection.requestMethod = mRequestMethod.value
                connection.doOutput = mRequestMethod != RequestMethod.GET
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-length", postData.size.toString())
                connection.setRequestProperty("Content-Type", mContentType.value)


                try {
                    val outputStream = DataOutputStream(connection.outputStream)
                    outputStream.write(postData)
                    outputStream.flush()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

                try {
                    val inputstream = DataInputStream(connection.inputStream)
                    val reader = BufferedReader(InputStreamReader(inputstream))

                    val response = reader.readLine()

                    // Pause volontaire pour simuler une requête "longue"
                    SystemClock.sleep(20000)

                    handler.post {
                        mListener.get()?.handleServerResponse(response)
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }
}