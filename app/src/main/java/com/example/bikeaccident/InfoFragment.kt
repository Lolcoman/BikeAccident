package com.example.bikeaccident

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.databinding.FragmentInfoBinding
import com.google.gson.Gson


open class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel:FragmentViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonn = pref.getString("MyObject", "")

//        val view = binding.root

        val recyclerView: RecyclerView = binding.RecyclerView
        /*viewModel.accident2.observe(viewLifecycleOwner, Observer{ accident2 ->
            *//*recyclerView.adapter = AccidentAdapter(accident2)
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            recyclerViewAdapter.notifyDataSetChanged()*//*
            recyclerView.apply {
//            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = AccidentAdapter(accident2)
            }
        })*/
//        viewModel.accident2.value?.add(Accident(1,2010,"ANO!!","Brno"))
        viewModel.accident2.observe(viewLifecycleOwner, Observer{ accident2 ->
                recyclerView.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = AccidentAdapter(accident2)
                }
        })
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity?.applicationContext, "Žádné nehody!", Toast.LENGTH_LONG).show()
            }
        })
        /*recyclerView.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
//            adapter = AccidentAdapter()
        }*/
        /*viewModel.getRecyclerListDataObserver().observe(viewLifecycleOwner, Observer{
            recyclerView.adapter = AccidentAdapter(it)
            recyclerViewAdapter.notifyDataSetChanged()
        })*/
        //RECYCLER VIEWER
        /*val recyclerView: RecyclerView = binding.RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = AccidentAdapter(viewModel.accident)
        }
        viewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        viewModel.getRecyclerListDataObserver().observe(viewLifecycleOwner, Observer<MutableList<Accident>> {
            recyclerView.adapter = AccidentAdapter(it)
            recyclerViewAdapter.notifyDataSetChanged()
        })*/
//        recyclerView.adapter = AccidentAdapter(viewModel.accident)

        /*viewModel._accident2.observe(viewLifecycleOwner, Observer {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity?.applicationContext)
//                adapter = AccidentAdapter(viewModel.accident)
                adapter = AccidentAdapter(it)
            }
        })*/
        /*viewModel.accident2.observe(viewLifecycleOwner, Observer {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity?.applicationContext)
//                adapter = AccidentAdapter(viewModel.accident)
                adapter = AccidentAdapter(it)
            }
        })*/
        val gsonn = Gson()
        val sharedData = gsonn.fromJson(jsonn, DataResponse::class.java)
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
        binding.searchButton?.setOnClickListener {
            var rokText = binding.editTextYear
            var rok: Int
            if(rokText?.text.toString() == "" || rokText?.text.toString().toInt() < 2010 || rokText?.text.toString().toInt() > 2021)
            {
                rokText?.error = "Zadejte rok 2010-2021";
                //Toast.makeText(activity?.applicationContext, "Zadejte rok!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                rok = rokText?.text.toString().toInt()
            }
            var groupAlkohol = binding.alkoholGroup
            var alkoholSelected = groupAlkohol!!.checkedRadioButtonId
            var alkohol = view.findViewById(alkoholSelected) as RadioButton
            alkohol.text
            val alc = alkohol.text.toString()



//            searchAccident(rok, alc, sharedData)
            /*recyclerView.apply {
                layoutManager = LinearLayoutManager(activity?.applicationContext)
                adapter = null
            }*/
            viewModel.searchAccident(rok,alc,sharedData)

            /*viewModel.accident2.observe(viewLifecycleOwner, Observer{ accident2 ->
                recyclerView.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.hasFixedSize()
                    it.adapter = AccidentAdapter(accident2)
                }
            })*/
        }

        binding.mapShow?.setOnClickListener {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        //val mPrefs : SharedPreferences?= activity?.getPreferences(Context.MODE_PRIVATE);
        binding = FragmentInfoBinding.inflate(layoutInflater,container,false)
        /*val recyclerView: RecyclerView = binding.RecyclerView
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val jsonn = pref.getString("MyObject", "")
        val gsonn = Gson()
        val sharedData = gsonn.fromJson(jsonn, DataResponse::class.java)
        viewModel.searchAccident(2010,"Ano",sharedData)
        viewModel.accident2.observe(viewLifecycleOwner, Observer{ accident2 ->
            recyclerView.adapter = AccidentAdapter(accident2)
            recyclerViewAdapter.notifyDataSetChanged()
        })*/
//        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        val jsonn = pref.getString("MyObject", "")
//
//        val view = binding.root
//
//        //RECYCLER VIEWER
//        val recyclerView: RecyclerView = binding.RecyclerView
////        recyclerView.apply {
////            layoutManager = LinearLayoutManager(activity?.applicationContext)
////            adapter = AccidentAdapter(viewModel.accident)
////        }
////        recyclerView.adapter = AccidentAdapter(viewModel.accident)
//        val gsonn = Gson()
//        val sharedData = gsonn.fromJson(jsonn, DataResponse::class.java)
//        //Surový list
//        val list: ArrayList<String> = ArrayList()
//        //Převedený líst
//        val decodeList: ArrayList<String> = ArrayList()
//        for (i in 0 until sharedData.features.size ){
//            list.add(sharedData.features[i].properties.pricina)
//        }
//        //Nastavení správného kódování do českého jazyka
//        for (i in 0 until list.size ){
//            decodeList.add(String( list[i].toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
//        }
//        binding.searchButton.setOnClickListener {
//            var rokText = binding.editTextYear
//            var rok: Int
//            if(rokText.text.toString() == "" || rokText.text.toString().toInt() < 2010 || rokText.text.toString().toInt() > 2021)
//            {
//                rokText.error = "Zadejte rok 2010-2021";
//                //Toast.makeText(activity?.applicationContext, "Zadejte rok!", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }else{
//                rok = rokText.text.toString().toInt()
//            }
//            var groupAlkohol = binding.alkoholGroup
//            var alkoholSelected = groupAlkohol.checkedRadioButtonId
//            var alkohol = view.findViewById(alkoholSelected) as RadioButton
//            alkohol.text
//            val alc = alkohol.text.toString()
//
//
//            viewModel.searchAccident(rok,alc,sharedData)
////            searchAccident(rok, alc, sharedData)
//            recyclerView.apply {
//                layoutManager = LinearLayoutManager(activity?.applicationContext)
//                adapter = AccidentAdapter(viewModel.accident)
//            }
//        }
//
//        binding.mapShow.setOnClickListener {
////            findNavController().navigate(binding.navHostFragment.id)
//            /*val fragment = MapsFragment()
//            val transaction = fragmentManager?.beginTransaction()
//            transaction?.replace(binding.fragmentContainerView4.id,fragment)?.commit()*/
//            /*val myLatitude = 49.171130437000045
//            val myLongitude = 16.520742066000025
//            val gmmIntentUri =
//                Uri.parse("https://www.google.com/maps/dir/?api=1&origin= 49.171130437000045,16.520742066000025&destination=49.26102991,16.57131051&waypoints=49.22375737,16.62980964&travelmode=driving")
//            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            intent.setPackage("com.google.android.apps.maps")
//            try {
//                startActivity(intent)
//            } catch (ex: ActivityNotFoundException) {
//                try {
//                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                    startActivity(unrestrictedIntent)
//                } catch (innerEx: ActivityNotFoundException) {
//                    Toast.makeText(activity?.applicationContext, "Prosím nainstalujte si Mapy Google", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }*/
//
//            Navigation.findNavController(view).navigate(com.example.bikeaccident.R.id.action_infoFragment_to_mapsFragment)
//           /* val fragment2 = MapsFragment()
//            val fragmentManager = fragmentManager
//            val fragmentTransaction = fragmentManager!!.beginTransaction()
//            fragmentTransaction.replace(binding.fragmentContainerView.id, fragment2)
//            fragmentTransaction.commit()*/
//        }
        return  binding.root
    }
}