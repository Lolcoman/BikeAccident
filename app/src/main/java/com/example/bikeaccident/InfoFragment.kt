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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.databinding.FragmentInfoBinding
import com.google.gson.Gson

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

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
        //val spn = binding.spinner
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
        //spn.adapter = arrayAdapter
        //ar rok = binding.editTextYear.text.toString().toInt()

        //view.visibility = View.GONE;

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

            searchAccident(rok,alc,sharedData)
        }

        binding.mapShow.setOnClickListener {
            /*val myLatitude = 49.171130437000045
            val myLongitude = 16.520742066000025
            val gmmIntentUri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&origin= 49.171130437000045,16.520742066000025&destination=49.26102991,16.57131051&waypoints=49.22375737,16.62980964&travelmode=driving")
            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    Toast.makeText(activity?.applicationContext, "Prosím nainstalujte si Mapy Google", Toast.LENGTH_LONG)
                        .show()
                }
            }*/
            /*val fragment2 = MapsFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.inf, fragment2)
            fragmentTransaction.commit()*/
        }
        return  view
    }
    private fun searchAccident(rok: Int,alkohol: String,sharedData: DataResponse)
    {
        var featury = sharedData.features
        val featureList = featury.filter {
            try{
                it.properties.rok == rok && it.properties.alkohol.startsWith(alkohol) //&&
                       /* it.properties.pricina == asciiEncodedString &&
                        it.properties.ozn_osoba.startsWith(prilba) &&
                        it.properties.nasledek.startsWith(nasledek) &&
                        it.properties.povetrnostni_podm.contains(povetrnostniPodminky) &&
                        it.properties.viditelnost.startsWith(noc)*/
            }
            catch (e: Exception){
                Toast.makeText(activity?.applicationContext, "Žádný záznam", Toast.LENGTH_SHORT).show()
                return
            }
        }
        Toast.makeText(activity?.applicationContext, "Počet záznamů:" + featureList.size.toString(), Toast.LENGTH_SHORT).show()
        println(featureList.size)
    }
}