package com.example.bikeaccident

import androidx.lifecycle.ViewModel
import com.example.bikeaccident.Models.DataResponse

class FragmentViewModel: ViewModel() {
    var itemsList: ArrayList<String>
    private lateinit var customAdapter: CustomAdapter
    init {
        itemsList = ArrayList<String>()
    }

//    fun getRecyclerListDataObserver(): MutableLiveData<RecyclerList>{
//        return itemsList
//    }


    fun searchAccident(rok: Int,alkohol: String,sharedData: DataResponse)
    {
        itemsList.clear()
        var featury = sharedData.features
        val featureList = featury.filter {
                it.properties.rok == rok && it.properties.alkohol.startsWith(alkohol)
        }
        for (item in featureList){
            itemsList.add(item.properties.objectid.toString())
            itemsList.add(item.properties.rok.toString())
            itemsList.add(String(item.properties.alkohol.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
            itemsList.add(String(item.properties.misto_nehody.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
//            itemsList.add(String(item.properties.pricina.toByteArray(charset("ISO-8859-1")), charset("UTF-8")))
        }
        customAdapter.notifyDataSetChanged()
//        Toast.makeText(activity?.applicationContext, "Počet záznamů:" + featureList.size.toString(), Toast.LENGTH_SHORT).show()
        //binding.RecyclerView
//        println(featureList.size)
    }
}