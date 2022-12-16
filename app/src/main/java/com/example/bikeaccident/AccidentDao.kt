package com.example.bikeaccident

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bikeaccident.Models.PropertiesX


@Dao
interface AccidentDao {

//    @Query("SELECT * FROM nehody")
//    suspend fun getAll(): List<PropertiesX>

    @Insert
    fun insertAll(prop:List<PropertiesX>)
}
