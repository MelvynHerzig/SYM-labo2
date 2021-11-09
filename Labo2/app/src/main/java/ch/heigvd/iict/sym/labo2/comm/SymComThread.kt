package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

/**
 * Classe implémentant un thread de communication http pour le SymComManager
 * @param listener Méthode à appeler une fois la réponse reçue
 * @param request Requête à transmettre
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
class SymComThread(
    private val listener: WeakReference<CommunicationEventListener>,
    private val request: SymComRequest
) : Thread() {

    /**
     * Classe interne "statique" pour permettre la libération du SymComThread
     * dès que le post sur le handler a été effectué sans pour autant avoir été traité
     */
    class ResponseRunnable(
        private val listener: WeakReference<CommunicationEventListener>,
        val response: ByteArray
    ) : Runnable {

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

            val outputStream : OutputStream
            if (request.isCompressed) {
                connection.setRequestProperty("X-Network", "CSD")
                connection.setRequestProperty("X-Content-Encoding", "deflate")

                outputStream = DeflaterOutputStream(connection.outputStream)
            } else {
                outputStream = DataOutputStream(connection.outputStream)
            }

            try {
                outputStream.write(postData)
                outputStream.flush()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            val inputstream : InputStream = if(request.isCompressed) { InflaterInputStream(connection.inputStream)} else {DataInputStream(connection.inputStream)}

            try {
                val bytes = inputstream.readBytes()
                Handler(Looper.getMainLooper()).post(ResponseRunnable(listener, bytes))

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


