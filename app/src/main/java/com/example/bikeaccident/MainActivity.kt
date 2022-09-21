package com.example.bikeaccident

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.buttonFetch)
        btn.setOnClickListener {
            downloadTask()
        }
    }

    private fun downloadTask(){
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET,url,
            Response.Listener { response ->
                val data = response.toString()
                var jArray = JSONArray(data)
                Log.e("Error",jArray.toString())
            },
            Response.ErrorListener {  })
        queue.add(request)
    }
}