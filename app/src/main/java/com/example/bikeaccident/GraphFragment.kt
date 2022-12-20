package com.example.bikeaccident

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.PropertiesX
import com.example.bikeaccident.databinding.FragmentGraphBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class GraphFragment : Fragment() {

    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    private lateinit var barList: ArrayList<BarEntry>
    private lateinit var binding: FragmentGraphBinding
    private lateinit var barChart: BarChart
    private lateinit var lineDataSet: BarDataSet
    private var fragmentGraphBinding: FragmentGraphBinding? = null
    private lateinit var appDd: AccidentDatabase

    private  fun writeData(dataResponse: PropertiesX){
        GlobalScope.launch(Dispatchers.IO){
            appDd.accidentDao().insertAll(dataResponse)
        }
    }
    //Běží v hlavním vlákně DAO
    private fun getYear(year: Int) :Int{
        return appDd.accidentDao().getYear(year)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(layoutInflater)
        fragmentGraphBinding = binding //if the view is already inflated then we can just bind it to view binding.
        val view = binding.root
        appDd = AccidentDatabase.getDatabase(this.requireActivity())
        barChart = binding.idBarChart
//        downloadTask()
        getYearGraph()

        val btn = binding.buttonFetch
        btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_graphFragment_to_infoFragment)
        }
        return view
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentGraphBinding = null
        super.onDestroyView()
    }

    //Funkce pro vykreslení grafu podle roků
    private fun getYearGraph() {
        val featureList: MutableList<Int> = mutableListOf()
        barList = ArrayList()
        val year = ArrayList<String>()
        val averageAccident: MutableList<Int> = mutableListOf()
        val yearNow = Calendar.getInstance().get(Calendar.YEAR);
        for ((counter, i) in (2010 until yearNow).withIndex()) {
            featureList.add(getYear(i))
            println(featureList[counter])
            barList.add(BarEntry(featureList[counter].toFloat(), counter))
            averageAccident.add(featureList[counter])
            year.add(i.toString())
        }
        lineDataSet = BarDataSet(barList,"Průměrně nehod: " + averageAccident.average().toInt())
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
        if (appDd.accidentDao().exists(1)) {
            return
        }
        else {
            val queue = Volley.newRequestQueue(this.requireActivity())
            val request = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val apiData = Gson().fromJson(response, DataResponse::class.java)
                    //ZÁPIS DO ROOM DATABÁZE
                    for (i in 0 until apiData.features.size) {
                        writeData(apiData.features[i].properties)
                    }
                    //getYearGraph()
                },
                { })
            queue.add(request)
        }
    }
}