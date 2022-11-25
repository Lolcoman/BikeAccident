package com.example.bikeaccident

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.databinding.FragmentInfoBinding
import com.google.gson.Gson


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
    lateinit var languagesList: ArrayList<String>

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
        val test = gsonn.fromJson(jsonn, DataResponse::class.java)
        val spn = binding.spinner
        //Surový list
        val list: ArrayList<String> = ArrayList()
        //Převedený líst
        val decodeList: ArrayList<String> = ArrayList()
        for (i in 0 until test.features.size ){
            list.add(test.features[i].properties.pricina)
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

        var alkohol = binding.alkoholGroup

        // get selected radio button from radioGroup
        //val selectedId: Int = alkohol.checkedRadioButtonId
        var spinner = binding.spinner
        var helma = binding.helma
        var pocasi = binding.pocasi
        var zraneni = binding.zraneni
        var komunikace = binding.komunikace

        binding.searchButton.setOnClickListener {
            //searchAccident(rok,spinner.selectedItem.toString())
        }
        return  view
    }
private fun searchAccident(rok: Int,alkohol: String,spinner: String, jine: String)
{

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