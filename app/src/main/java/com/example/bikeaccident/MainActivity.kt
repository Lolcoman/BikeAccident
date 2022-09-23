package com.example.bikeaccident

import android.graphics.Color
import android.net.wifi.p2p.WifiP2pManager.ServiceResponseListener
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
import java.time.Year
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    lateinit var barList: ArrayList<BarEntry>
    lateinit var barChart: BarChart
    lateinit var lineDataSet: BarDataSet
    lateinit var barData: BarData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.buttonFetch)
        barChart = findViewById(R.id.idBarChart)
        btn.setOnClickListener {
            barChart.invalidate()
            downloadYear()
        }

        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET,url,
            { response ->
                val apiData = Gson().fromJson(response, DataResponse::class.java)
                val features = apiData.features

                fun downloadYear(year: Int): Float{
                    val featureList = features.filter{
                        it.properties.rok == year
                    }
                    return featureList.size.toFloat()
                }
                barList = ArrayList()
                barList.add(BarEntry(downloadYear(2010), 0))
                barList.add(BarEntry(downloadYear(2011), 1))
                barList.add(BarEntry(downloadYear(2012), 2))
                barList.add(BarEntry(downloadYear(2013), 3))
                lineDataSet = BarDataSet(barList,"Počet nehod za rok")
        /*
        barList = ArrayList()
        barList.add(BarEntry(downloadTask(2010), 0))
        barList.add(BarEntry(2f, 1))
        barList.add(BarEntry(5f, 2))
        barList.add(BarEntry(20f, 3))
        lineDataSet = BarDataSet(barList,"Počet nehod za rok")
        //lineDataSet.setValueFormatter(getFormattedValue(barList.get(1)))
        */
        val year = ArrayList<String>()
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        val xAxis: XAxis = barChart.xAxis
        xAxis.textSize = 15f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        barChart.setDescription("")
        val data = BarData(year,lineDataSet)
        lineDataSet.setValueFormatter { value, entry, dataSetIndex, viewPortHandler -> value.roundToInt().toString() }
        //JEN LEGENDA
        val l: Legend = barChart.legend
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 15f
        l.textSize = 20f
        barChart.data = data
        barChart.animateY(3000)
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 20f
        barChart.setScaleEnabled(false)

            },
            { })
        queue.add(request)
    }

    private fun downloadYear(){
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET,url,
            { response ->
                val apiData = Gson().fromJson(response, DataResponse::class.java)
                val features = apiData.features
                val featureList = features.filter{
                    it.properties.rok == 2000
                }
                for (rok in features){
                    println(rok)
                }
                //println(features.filter { })
            },
            { })
        queue.add(request)
    }
}

