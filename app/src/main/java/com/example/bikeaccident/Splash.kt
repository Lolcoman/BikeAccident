package com.example.bikeaccident

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.PropertiesX
import com.google.gson.Gson
import kotlinx.coroutines.*

class Splash : AppCompatActivity() {
    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"
    private lateinit var appDd: AccidentDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appDd = AccidentDatabase.getDatabase(this)
        supportActionBar?.hide()
        downloadTask()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },3500) //3,5 sekundy
    }

    private fun writeData(dataResponse: PropertiesX){
        GlobalScope.launch(Dispatchers.IO){
            appDd.accidentDao().insertAll(dataResponse)
        }
    }
    private fun downloadTask() {
        if (appDd.accidentDao().exists(1)) {
            return
        }
        else {
            val queue = Volley.newRequestQueue(this)
            val request = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val apiData = Gson().fromJson(response, DataResponse::class.java)
                    //ZÁPIS DO ROOM DATABÁZE
                    for (i in 0 until apiData.features.size) {
                        writeData(apiData.features[i].properties)
                    }
                },
                { })
            queue.add(request)
        }
    }
}
