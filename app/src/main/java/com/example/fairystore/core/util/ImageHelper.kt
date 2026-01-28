package com.example.fairystore.core.util

import android.R
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL

object ImageHelper {

    fun imageLoader(image: ImageView, url: String){
        Thread{
            try{
                val conn: HttpURLConnection
                val url = URL(url)
                conn = url.openConnection() as HttpURLConnection

                val bitmap = BitmapFactory.decodeStream(conn.inputStream)
                image.post {
                    image.setImageBitmap(bitmap)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }.start()
    }
}