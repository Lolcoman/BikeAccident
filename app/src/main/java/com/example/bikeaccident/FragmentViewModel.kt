package com.example.bikeaccident

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bikeaccident.Models.PropertiesX

class FragmentViewModel: ViewModel() {
    private val _accident2 = MutableLiveData<MutableList<Accident>>()

    val accident2: LiveData<MutableList<Accident>>
        get() = _accident2

    var status = MutableLiveData<Boolean?>()

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

    fun searchAccident(rok: Int, alkohol: String, props: List<PropertiesX>)
    {
        accident2.value?.clear()
        var featury = props
        val featureList = featury.filter {
            try{
                var alc = String(it.alkohol!!.toByteArray(charset("ISO-8859-1")),
                    charset("UTF-8"))
                when (alc){
                    "Ne" -> it.rok == rok && alc == alkohol
                    "Nezjišťován" -> it.rok == rok && alc == alkohol
                    else -> {
                        it.rok == rok && alc.startsWith(alkohol)
                    }
                }
            }
            catch (e: Exception){
                println(e)
                return
            }
        }
        if (featureList.isEmpty()){
            status.value = true
            return
        }
        for (item in featureList){
            if (item.nazev.isNullOrEmpty())
            {
                _accident2.addNewItem(Accident(item.objectid!!,item.rok,
                    String(item.alkohol!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8")),
                    "Neuvedeno"))
                return
            }else{
            _accident2.addNewItem(Accident(item.objectid!!,item.rok,
                String(item.alkohol!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8")),
                String(item.nazev!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))))
            }
        }
    }
}