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
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Runnable qui exécute une communication avec un serveur
 * @param mUrl Serveur sur lequel se connecter.
 * @param mRequest Contenu.
 * @param mContentType Type de contenu (en header http)
 * @param mRequestMethod type de requête http.
 * @param mListener Listener à notifier une fois la réponse reçue.
 */
class SymComRunnable (val mUrl          : String,
                      val mRequest      : String,
                      val mContentType  : ContentType,
                      val mRequestMethod: RequestMethod,
                      val mListener     : WeakReference<CommunicationEventListener>) : Runnable {

    override fun run() {

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
                SystemClock.sleep(2000)

                Handler(Looper.getMainLooper()).post {
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