/**
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Herzig Melvyn
 */
package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Classe implémentant un thread de communication http pour le SymComManager
 * @param listener Méthode à appeler une fois la réponse reçue
 * @param request Requête à transmettre
 */
class SymComThread(val listener : WeakReference<CommunicationEventListener>,
                   val request  : SymComRequest) : Thread() {

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
                val reader = BufferedReader(InputStreamReader(inputstream))

                val response = reader.readLine()

                // Pause volontaire pour simuler une requête "longue"
                SystemClock.sleep(2000)

                Handler(Looper.getMainLooper()).post {
                    listener.get()?.handleServerResponse(response)
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


