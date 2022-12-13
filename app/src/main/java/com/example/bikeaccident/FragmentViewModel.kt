package com.example.bikeaccident

import androidx.lifecycle.ViewModel
import com.example.bikeaccident.Models.DataResponse

class FragmentViewModel: ViewModel() {
    var itemsList: ArrayList<String>
    init {
        itemsList = ArrayList<String>()
    }

//    fun getRecyclerListDataObserver(): MutableLiveData<RecyclerList>{
//        return itemsList
//    }



}