package com.example.bikeaccident

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
import com.example.bikeaccident.Models.PropertiesX
import com.example.bikeaccident.databinding.FragmentInfoBinding


open class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel:FragmentViewModel
    private lateinit var appDd: AccidentDatabase

    private  fun getAllFeatures(): List<PropertiesX>{
            return appDd.accidentDao().getAll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.RecyclerView
        viewModel.accident2.observe(viewLifecycleOwner, Observer{ accident2 ->
                recyclerView.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = AccidentAdapter(accident2)
                }
        })
        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                viewModel.status.value = null
                Toast.makeText(activity?.applicationContext, "Žádné nehody!", Toast.LENGTH_LONG).show()
            }
        })
        binding.searchButton?.setOnClickListener {
            var rokText = binding.editTextYear
            var rok: Int
            if(rokText?.text.toString() == "" || rokText?.text.toString().toInt() < 2010 || rokText?.text.toString().toInt() > 2021)
            {
                rokText?.error = "Zadejte rok 2010-2021";
                return@setOnClickListener
            }else{
                rok = rokText?.text.toString().toInt()
            }
            var groupAlkohol = binding.alkoholGroup
            var alkoholSelected = groupAlkohol!!.checkedRadioButtonId
            var alkohol = view.findViewById(alkoholSelected) as RadioButton
            alkohol.text
            val alc = alkohol.text.toString()
            viewModel.searchAccident(rok,alc,getAllFeatures())
            Toast.makeText(activity?.applicationContext, "Počet nehod: " + viewModel.accident2.value?.size, Toast.LENGTH_LONG).show()
        }

        binding.mapShow?.setOnClickListener {
            val bundle = Bundle()
            var rokText = binding.editTextYear
            bundle.putString("rok",rokText?.text.toString())
            var groupAlkohol = binding.alkoholGroup
            var alkoholSelected = groupAlkohol!!.checkedRadioButtonId
            var alkohol = view.findViewById(alkoholSelected) as RadioButton
            alkohol.text
            val alc = alkohol.text.toString()
            bundle.putString("alkohol",alc)
            val fragment = MapsFragment()
            fragment.arguments = bundle

            Navigation.findNavController(view).navigate(com.example.bikeaccident.R.id.action_infoFragment_to_mapsFragment,bundle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        appDd = AccidentDatabase.getDatabase(this.requireActivity())
        binding = FragmentInfoBinding.inflate(layoutInflater,container,false)
        return  binding.root
    }
}