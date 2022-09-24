package com.example.bikeaccident

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.gson.Gson
import java.text.DecimalFormat
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {


    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    lateinit var barList: ArrayList<BarEntry>
    //lateinit var year: ArrayList<String>
    lateinit var barChart: BarChart
    lateinit var lineDataSet: BarDataSet
    lateinit var barData: BarData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.buttonFetch)
        barChart = findViewById(R.id.idBarChart)
        btn.setOnClickListener {
            //downloadTask()
            barChart.invalidate()
        }

        barList = ArrayList()
        barList.add(BarEntry(8f, 0))
        barList.add(BarEntry(2f, 1))
        barList.add(BarEntry(5f, 2))
        barList.add(BarEntry(20f, 3))
        lineDataSet = BarDataSet(barList,"Počet nehod")
        //lineDataSet.setValueFormatter(getFormattedValue(barList.get(1)))

        val year = ArrayList<String>()
        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        val xAxis: XAxis = barChart.xAxis
        //xAxis.position = XAxisPosition.BOTTOM
        xAxis.textSize = 15f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        barChart.setDescription("")
        val data = BarData(year,lineDataSet)
        lineDataSet.setValueFormatter { value, entry, dataSetIndex, viewPortHandler -> value.roundToInt().toString() }
        //JEN LEGENDA
        val l: Legend = barChart.legend
        //l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        //l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        //l.orientation = Legend.LegendOrientation.HORIZONTAL
        //l.setDrawInside(false)
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 15f
        l.textSize = 20f
        //l.xEntrySpace = 10f
        //l.yEntrySpace = 20f

        //barData = BarData(lineDataSet)
        barChart.data = data
        barChart.animateY(3000)
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 20f
        barChart.setScaleEnabled(false)
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

