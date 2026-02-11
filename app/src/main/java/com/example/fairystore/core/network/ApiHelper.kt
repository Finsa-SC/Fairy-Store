package com.example.fairystore.core.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.os.unregisterForAllProfilingResults
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiHelper {
    private const val baseUrl = "https://fakestoreapi.com/"
    private val handler = Handler(Looper.getMainLooper())

    enum class HttpMethod{
        POST,
        PUT,
        GET,
        DELETE
    }

    sealed class ApiResult{
        data class Success (val jsonBody: String): ApiResult()
        data class Empty(val msg: String = "Server Return Empty Data"): ApiResult()
        data class Error(val code: Int, val msg: String): ApiResult()
    }

    private fun request(
        context: Context,
        method: HttpMethod,
        endpoint: String,
        jsonBody: JSONObject? = null,
        callback: (ApiResult) -> Unit)
    {
        Thread {
            var conn: HttpURLConnection? = null
            val result = try {


                conn = (URL(baseUrl + endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = method.name
                    readTimeout = 5000
                    connectTimeout = 5000
                    doInput = true
                    if (jsonBody != null) {
                        setRequestProperty("Content-Type", "application/json")
                        doOutput = true
                    }
                }

                val pref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val token = pref.getString("token", null)

                if(token != null){
                    conn.setRequestProperty("Authorization", "Bearer $token")
                }

                jsonBody?.let {
                    conn.outputStream.use { outputStream ->
                        outputStream.write(it.toString().toByteArray())
                    }
                }
                val code = conn.responseCode

                val text = try {
                    if (code in 200..299) {
                        conn.inputStream.bufferedReader().use { it.readText() }
                    } else {
                        conn.errorStream?.bufferedReader()?.use { it.readText() }
                    }
                } catch (_: Exception) {
                    null
                }

                when {
                    code in 200..299 && text.isNullOrBlank() ->
                        ApiResult.Empty()

                    code in 200..299 ->
                        ApiResult.Success(text.toString())

                    else -> {
                        val message = try {
                            text?.let {
                                text
                            } ?: "Internal Server Error"
                        } catch (_: Exception) {
                            "Internal Server Error"
                        }
                        ApiResult.Error(code, message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ApiResult.Error(-1, "n ")
            }
            handler.post { callback(result) }
        }.start()
    }

    fun post(context: Context, endpoint: String, jsonBody: JSONObject? = null, callback: (ApiResult) -> Unit) = request(context, HttpMethod.POST, endpoint, jsonBody, callback)
    fun get(context: Context, endpoint:String, callback: (ApiResult) -> Unit) = request(context, HttpMethod.GET, endpoint, callback = callback)
}