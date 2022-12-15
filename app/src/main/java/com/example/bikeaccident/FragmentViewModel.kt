package com.example.bikeaccident

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikeaccident.Models.DataResponse

class FragmentViewModel: ViewModel() {
    var accident = mutableListOf<Accident>()
    private val _accident2 = MutableLiveData<MutableList<Accident>>()

    val accident2: LiveData<MutableList<Accident>>
        get() = _accident2

    fun getRecyclerListDataObserver(): MutableLiveData<MutableList<Accident>>{
        return _accident2
    }

    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

    fun searchAccident(rok: Int, alkohol: String, sharedData: DataResponse)
    {
        accident.clear()
        var featury = sharedData.features
        val featureList = featury.filter {
            try{
                var alc = String(it.properties.alkohol.toByteArray(charset("ISO-8859-1")),
                    charset("UTF-8"))
                when (alc){
                    "Ne" -> it.properties.rok == rok && alc == alkohol
                    "Nezjišťován" -> it.properties.rok == rok && alc == alkohol
                    else -> {
                        it.properties.rok == rok && alc.startsWith(alkohol)
                    }
                }
            }
            catch (e: Exception){
                println(e)
                return
            }
        }
        if (featureList.isEmpty()){
//            recyclerView.adapter?.notifyDataSetChanged()
            return
        }
        accident.add(Accident(1,2010,"ANO!!","Brno"))
        accident2.value?.add(Accident(1,2010,"ANO!!","Brno"))
//        for (item in featureList){
//            accident.add(Accident(item.properties.objectid,item.properties.rok,
//                String(item.properties.alkohol.toByteArray(charset("ISO-8859-1")), charset("UTF-8")),
//                String(item.properties.nazev.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))))
//        }
        //RECYCLER VIEWER
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(activity?.applicationContext)
//            adapter = AccidentAdapter(accident)
//        }
    }
}