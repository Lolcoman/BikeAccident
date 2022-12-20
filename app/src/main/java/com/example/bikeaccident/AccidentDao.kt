package com.example.bikeaccident

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bikeaccident.Models.PropertiesX


@Dao
interface AccidentDao {

    @Query("SELECT * FROM nehody")
    fun getAll(): List<PropertiesX>

    @Query("SELECT COUNT(*) FROM nehody WHERE rok = :year")
    fun getYear(year: Int?): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(prop:PropertiesX)

    @Query("SELECT EXISTS (SELECT 1 FROM nehody WHERE objectid = :id)")
    fun exists(id: Int): Boolean
}
