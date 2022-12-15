package com.example.bikeaccident

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikeaccident.Models.DataResponse

class FragmentViewModel: ViewModel() {
    private val _accident2 = MutableLiveData<MutableList<Accident>>()

    val accident2: LiveData<MutableList<Accident>>
        get() = _accident2

    private val statusMessage = MutableLiveData<Event<Boolean>>()

    val message : LiveData<Event<Boolean>>
        get() = statusMessage

    fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
        val oldValue = this.value ?: mutableListOf()
        oldValue.add(item)
        this.value = oldValue
    }

    fun <T> MutableLiveData<MutableList<T>>.addNewItemAt(index: Int, item: T) {
        val oldValue = this.value ?: mutableListOf()
        oldValue.add(index, item)
        this.value = oldValue
    }

    fun <T> MutableLiveData<MutableList<T>>.removeItemAt(index: Int) {
        if (!this.value.isNullOrEmpty()) {
            val oldValue = this.value
            oldValue?.removeAt(index)
            this.value = oldValue
        } else {
            this.value = mutableListOf()
        }
    }

    fun searchAccident(rok: Int, alkohol: String, sharedData: DataResponse)
    {
        accident2.value?.clear()
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
            statusMessage.value = Event(false)
            return
        }
        for (item in featureList){
            _accident2.addNewItem(Accident(item.properties.objectid,item.properties.rok,
                String(item.properties.alkohol.toByteArray(charset("ISO-8859-1")), charset("UTF-8")),
                String(item.properties.nazev.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))))
        }
    }
}