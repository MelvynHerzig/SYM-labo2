package ch.heigvd.iict.sym.labo2.comm

import android.os.Handler
import android.os.Looper
import ch.heigvd.iict.sym.lab.comm.CommunicationEventListener
import ch.heigvd.iict.sym.labo2.manipulations.DelayedActivity
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class SymComManagerDelayed(var delayedActivity: DelayedActivity) {


    private var communicationEventListener: WeakReference<CommunicationEventListener?>? = null

    enum class ContentType(val value: String) {
        TEXT("text/plain"), JSON("application/json")
    }

    enum class RequestMethod(val value: String) {
        GET("GET"), POST("POST")
    }

    fun setCommunicationListener(listener : WeakReference<CommunicationEventListener?>){
        this.communicationEventListener = listener
    }

    fun sendRequest(
        url: String,
        request: String,
        contentType: ContentType,
        requestMethod: RequestMethod
    ) {
        val handler = Handler(Looper.getMainLooper())

        Thread {

            val connection = URL(url).openConnection() as HttpURLConnection


            connection.connectTimeout = 300000

            try {
                val postData = request.toByteArray(StandardCharsets.UTF_8)

                connection.requestMethod = requestMethod.value
                connection.doOutput = requestMethod != RequestMethod.GET
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-length", postData.size.toString())
                connection.setRequestProperty("Content-Type", contentType.value)


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

                    handler.post{

                        val cel = communicationEventListener?.get()
                        if (cel != null) {
                            cel.handleServerResponse(response)
                            println("NOT NULL NOT NULL")
                        }
                        else {
                            println("NULL NULL NULL")
                        }


                    }


                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }.start()
    }
}