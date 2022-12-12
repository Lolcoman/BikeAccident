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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.databinding.FragmentInfoBinding
import com.google.gson.Gson

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private val itemsList = ArrayList<String>()
    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var viewModel = ViewModelProvider(this).get(FragmentViewModel::class.java)
    }
    private val viewModel = FragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val mPrefs : SharedPreferences?= activity?.getPreferences(Context.MODE_PRIVATE);
        binding = FragmentInfoBinding.inflate(layoutInflater)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonn = pref.getString("MyObject", "")

        val view = binding.root

        //RECYCLER VIEWER
        val recyclerView: RecyclerView = binding.RecyclerView
        customAdapter = CustomAdapter(itemsList)
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

//        prepareItems()
//        val navHostFragment = activity?.supportFragmentManager
//            ?.findFragmentById(binding.fragmentContainerView4.id) as NavHostFragment
//        navController = navHostFragment.navController

        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_info,container,false)
        //text = view.findViewById(R.id.textView1)
        //val data = arguments
        val gsonn = Gson()
        //val jsonn = mPrefs?.getString("MyObject", "")
        val sharedData = gsonn.fromJson(jsonn, DataResponse::class.java)

        //itemsList.add(sharedData.features[0].properties.rok.toString())

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


            viewModel.searchAccident(rok, alc, sharedData)
        }

        binding.mapShow.setOnClickListener {
//            findNavController().navigate(binding.navHostFragment.id)
            /*val fragment = MapsFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(binding.fragmentContainerView4.id,fragment)?.commit()*/
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

            Navigation.findNavController(view).navigate(com.example.bikeaccident.R.id.action_infoFragment_to_mapsFragment)
           /* val fragment2 = MapsFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(binding.fragmentContainerView.id, fragment2)
            fragmentTransaction.commit()*/
        }
        return  view
    }
    private fun searchAccident(rok: Int,alkohol: String,sharedData: DataResponse)
    {
        itemsList.clear()
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
        for (item in featureList){
            itemsList.add(item.properties.objectid.toString())
            itemsList.add(item.properties.rok.toString())
            itemsList.add(String(item.properties.alkohol.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
            itemsList.add(String(item.properties.misto_nehody.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
//            itemsList.add(String(item.properties.pricina.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
        }
        customAdapter.notifyDataSetChanged()
        Toast.makeText(activity?.applicationContext, "Počet záznamů:" + featureList.size.toString(), Toast.LENGTH_SHORT).show()
        //binding.RecyclerView
        println(featureList.size)

    }

    // Call this in response to some state change in the VM
    private fun updateRecyclerView() {
        // Generate a list of "item viewmodels" to represent
        // each child item from the Fragment ViewModel's data state
        customAdapter = CustomAdapter(itemsList)
        customAdapter.notifyDataSetChanged()
    }
}