package com.example.bikeaccident

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.Feature
import com.example.bikeaccident.databinding.FragmentInfoBinding
import com.google.gson.Gson
import java.nio.charset.StandardCharsets


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    private lateinit var text : TextView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mPrefs : SharedPreferences?= activity?.getPreferences(Context.MODE_PRIVATE);
        binding = FragmentInfoBinding.inflate(layoutInflater)
        val view = binding.root
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_info,container,false)
        //text = view.findViewById(R.id.textView1)
        //val data = arguments
        val gsonn = Gson()
        val jsonn = mPrefs?.getString("MyObject", "")
        val sharedData = gsonn.fromJson(jsonn, DataResponse::class.java)
        val spn = binding.spinner
        //Surový list
        val list: ArrayList<String> = ArrayList()
        //Převedený líst
        val decodeList: ArrayList<String> = ArrayList()
        for (i in 0 until sharedData.features.size ){
            list.add(sharedData.features[i].properties.pricina)
        }
        //Nastavení správného kódování do českého jazyka
        for (i in 0 until list.size ){
            decodeList.add(String( list[i].toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
        }
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            R.layout.simple_spinner_dropdown_item,
            decodeList.distinct()
        )
        //println(decodeList[0])
        spn.adapter = arrayAdapter
        //ar rok = binding.editTextYear.text.toString().toInt()


        binding.searchButton.setOnClickListener {
            var rokText = binding.editTextYear
            //var rok = binding.editTextYear.text.toString().toInt()
            //var rok = rokText.text.toString().toInt()
            var rok: Int
            if(rokText.text.toString() == "" || rokText.text.toString().toInt() < 2010 || rokText.text.toString().toInt() > 2021)
            {
                rokText.error = "Zadejte rok 2010-2021";
                //Toast.makeText(activity?.applicationContext, "Zadejte rok!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                rok = rokText.text.toString().toInt()
            }
            //get selected radio button from radioGroup
            //val selectedId: Int = alkohol.checkedRadioButtonId
            var groupAlkohol = binding.alkoholGroup
            var alkoholSelected = groupAlkohol.checkedRadioButtonId
            var alkohol = view.findViewById(alkoholSelected) as RadioButton
            alkohol.text
            val alc = alkohol.text.toString()
            var spinner = binding.spinner
            //spinner.setSelection(0, false);

            var helma = binding.helma.isChecked
            var pocasi = binding.pocasi.isChecked
            var zraneni = binding.zraneni.isChecked
            var viditelnost = binding.viditelnost.isChecked

            searchAccident(rok,alc,spinner.selectedItem.toString(),helma, pocasi,zraneni,viditelnost,sharedData)
        }
        return  view
    }
private fun searchAccident(rok: Int,alkohol: String,spinner: String,helma: Boolean, pocasi: Boolean, zraneni: Boolean,viditelnost: Boolean,sharedData: DataResponse)
{
    var featury = sharedData.features
    var prilba = "bez"
    var nasledek = "nehoda pouze s hmotnou škodou"
    var povetrnostniPodminky = "neztížené"
    var noc = "ve dne"
    var encodedSpinner = spinner.toByteArray()
    var asciiEncodedString = String(encodedSpinner, StandardCharsets.ISO_8859_1)
    if (helma)
    {
        prilba = "s"
    }
    if (zraneni)
    {
        nasledek = "nehoda s následky na životě"
    }
    if (pocasi)
    {
        povetrnostniPodminky = "déšť"
    }
    if (viditelnost){
        noc = "v noci"
    }

    val featureList = featury.filter {
        try{
            it.properties.rok == null //&&
//                    it.properties.alkohol.startsWith(alkohol) &&
//                    it.properties.pricina == asciiEncodedString &&
//                    it.properties.ozn_osoba.startsWith(prilba) &&
//                    it.properties.nasledek.startsWith(nasledek) &&
//                    it.properties.povetrnostni_podm.contains(povetrnostniPodminky) &&
//                    it.properties.viditelnost.startsWith(noc)
        }
        catch (e: Exception){
            Toast.makeText(activity?.applicationContext, "Žádný záznam", Toast.LENGTH_SHORT).show()
            return
        }
    }
    Toast.makeText(activity?.applicationContext, "Počet záznamů:" + featureList.size.toString(), Toast.LENGTH_SHORT).show()
    println(featureList.size)
}
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                InfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}