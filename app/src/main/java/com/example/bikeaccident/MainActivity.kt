package com.example.bikeaccident

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.Feature
import com.example.bikeaccident.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {


    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    lateinit var barList: ArrayList<BarEntry>

    //lateinit var year: ArrayList<String>
    lateinit var barChart: BarChart
    lateinit var lineDataSet: BarDataSet
    lateinit var barData: BarData
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_main)
        setContentView(view)
        //val btn = findViewById<Button>(R.id.buttonFetch)
        val btn = binding.buttonFetch
        //barChart = findViewById(R.id.idBarChart)
        barChart = binding.idBarChart
        downloadTask()
        btn.setOnClickListener {
            //barChart.fitScreen()
            //val fragment = InfoFragment()
            val infoFragment = InfoFragment()
            val bundle = Bundle()
            bundle.putInt("year",12)
            val fragment : Fragment? =
            supportFragmentManager.findFragmentByTag(InfoFragment::class.java.simpleName)
            infoFragment.arguments = bundle
            if (fragment !is InfoFragment){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerForFirstFragment, infoFragment,InfoFragment::class.java.simpleName)
                    .commit()
            }
        }

    }

    //Funkce pro vykreslení grafu podle roků
    private fun getYearGraph(apiData: List<Feature>) {
        barList = ArrayList()
        val year = ArrayList<String>()
        var averageAccident: MutableList<Int> = mutableListOf<Int>()
        val yearNow = Calendar.getInstance().get(Calendar.YEAR);
        for ((counter, i) in (2010 until yearNow).withIndex()) {
            val featureList = apiData.filter {
                it.properties.rok == i
            }
            println("Rok: " + i + " Počet nehod: " + featureList.size)
            barList.add(BarEntry(featureList.size.toFloat(), counter))
            averageAccident.add(featureList.size)
            year.add(i.toString())
        }
        lineDataSet = BarDataSet(barList,"Průměrně nehod: " + averageAccident.average().toInt())
        //println(averageAccident.average())
        val xAxis: XAxis = barChart.xAxis
        xAxis.textSize = 15f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(true)
        xAxis.spaceBetweenLabels = 0
        lineDataSet.barSpacePercent = 30f
        barChart.setDescription("")
        val data = BarData(year, lineDataSet)
        lineDataSet.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
            value.roundToInt().toString()
        }
        //Nastavení legendy
        val l: Legend = barChart.legend
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 15f
        l.textSize = 15f
        //Nastavení sloupců
        barChart.data = data
        barChart.animateY(3000)
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 15f
        //barChart.setScaleEnabled(false)
    }

    private fun downloadTask() {
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, url,
            { response ->
                //val data = response.toString()
                //var jArray = JSONArray(data)
                //val apiData = Gson().fromJson(response, DataResponse::class.java)
                val apiData = Gson().fromJson(response, DataResponse::class.java)
                val features = apiData.features
                getYearGraph(features)

                /*
                for(item in features)
                {
                    if (item.properties.rok == 2010)
                    println(item.properties.rok)
                }
                val featureList = features.filter {
                   it.properties.rok == 2014
                }
                 */
                //features.forEach()
                //println(features)
                //featureList.forEach { println(it) }
                //val jsonObject = JSONTokener(response).nextValue() as JSONObject
                //val id = jsonObject.getJSONObject("crs")
                //val ie = jsonObject.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getString("rok")
                //Log.i("ROK: ", id)
                //println(ie)
            },
            { })
        queue.add(request)
    }
}

