package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

/**
 * Classe implémentant un thread de communication http pour le SymComManager
 * @param listener Méthode à appeler une fois la réponse reçue
 * @param request Requête à transmettre
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
*/
class SymComThread(private val listener : WeakReference<CommunicationEventListener>, private val request  : SymComRequest) : Thread() {

    /**
     * Classe interne "statique" pour permettre la libération du SymComThread
     * dès que le post sur le handler a été effectué sans pour autant avoir été traité
     */
    class ResponseStringRunnable(private val listener: WeakReference<CommunicationEventListener>, val response: String) : Runnable {

        override fun run() {
            listener.get()?.handleServerResponse(response)
        }
    }

    /**
     * Classe interne "statique" pour permettre la libération du SymComThread
     * dès que le post sur le handler a été effectué sans pour autant avoir été traité
     */
    class ResponseBytesRunnable(private val listener: WeakReference<CommunicationEventListener>, val response: ByteArray) : Runnable {

        override fun run() {
            listener.get()?.handleServerResponse(response)
        }
    }

    /**
     * Exécution
     */
    override fun run() {

        val connection = URL(request.url).openConnection() as HttpURLConnection

        connection.connectTimeout = 300000

        try {
            val postData = request.getBytesFromBody()

            connection.requestMethod = request.requestMethod.value
            connection.doOutput = request.requestMethod != RequestMethod.GET
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Content-length", postData.size.toString())
            connection.setRequestProperty("Content-Type", request.contentType.value)


            try {
                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.write(postData)
                outputStream.flush()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            try {
                val inputstream = DataInputStream(connection.inputStream)

                if (request.contentType == ContentType.PROTOBUF) {
                    val bytes = inputstream.readBytes()
                    Handler(Looper.getMainLooper()).post(ResponseBytesRunnable(listener, bytes))
                } else {
                    val reader = BufferedReader(InputStreamReader(inputstream))
                    val response = reader.readLine()
                    Handler(Looper.getMainLooper()).post(ResponseStringRunnable(listener, response))
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


