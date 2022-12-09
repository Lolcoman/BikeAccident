package com.example.bikeaccident

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.Feature
import com.example.bikeaccident.databinding.FragmentGraphBinding
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


class GraphFragment : Fragment() {

    private val url = "\t\n" +
            "https://data.brno.cz/datasets/mestobrno::cyklistick%C3%A9-nehody-bike-accidents.geojson?outSR=%7B%22latestWkid%22%3A5514%2C%22wkid%22%3A102067%7D"

    private lateinit var barList: ArrayList<BarEntry>
    private lateinit var navController: NavController
    private lateinit var binding: FragmentGraphBinding
    //lateinit var year: ArrayList<String>
    private lateinit var barChart: BarChart
    private lateinit var lineDataSet: BarDataSet
    lateinit var barData: BarData
    private var fragmentGraphBinding: FragmentGraphBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGraphBinding.bind(view)
        fragmentGraphBinding = binding //if the view is already inflated then we can just bind it to view binding.

        barChart = binding.idBarChart
        downloadTask()
        val btn = binding.buttonFetch
        btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_graphFragment_to_infoFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_graph, container,false)
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentGraphBinding = null
        super.onDestroyView()
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
            //println("Rok: " + i + " Počet nehod: " + featureList.size)
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
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        val mPrefs = this.requireActivity()
//            .getSharedPreferences("pref", Context.MODE_PRIVATE)
        //var mPrefs = getPreferences(AppCompatActivity.MODE_PRIVATE)
        val queue = Volley.newRequestQueue(this.requireActivity())
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                //val data = response.toString()
                //var jArray = JSONArray(data)
                //val apiData = Gson().fromJson(response, DataResponse::class.java)
                val apiData = Gson().fromJson(response, DataResponse::class.java)
                val features = apiData.features
                getYearGraph(features)

                val prefsEditor = pref.edit()
                val gson = Gson()
                val json = gson.toJson(apiData)
                prefsEditor.putString("MyObject", json)
                prefsEditor.commit()

                //JEN TEST ZDA JE DOBŘE ULOŽENO!
//                val gsonn = Gson()
//                val jsonn = mPrefs.getString("MyObject", "")
//                val test = gsonn.fromJson(jsonn, DataResponse::class.java)
//                println(test.features)
            },
            { })
        queue.add(request)
    }
}