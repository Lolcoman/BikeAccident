package com.example.bikeaccident

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import org.json.JSONObject
import org.json.JSONTokener

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

    //Funkce pro vykreslení grafu
    private fun setBarChartValues(){

    }

    private fun downloadTask(){
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET,url,
            { response ->
                //val data = response.toString()
                //var jArray = JSONArray(data)
                val apiData = Gson().fromJson(response, DataResponse::class.java)
                val features = apiData.features

                val featureList = features.filter{
                    it.properties.rok == 2014
                }

                println(featureList.size)
                //featureList.forEach { println(it) }
                //val jsonObject = JSONTokener(response).nextValue() as JSONObject
                //val id = jsonObject.getJSONObject("crs")
                //val ie = jsonObject.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getString("rok")
                //Log.i("ROK: ", id)
                //println(ie)
            },
            {  })
        queue.add(request)
    }
}