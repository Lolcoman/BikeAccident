package com.example.bikeaccident

import androidx.room.TypeConverter
import com.example.bikeaccident.Models.Geometry
import com.example.bikeaccident.Models.Properties
import com.example.bikeaccident.Models.PropertiesX
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class RoomConverters {
//    @TypeConverter
//    fun fromString(value: String?): ArrayList<String?>? {
//        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }

    @TypeConverter
    fun fromProp(prop: List<PropertiesX>): String {
        return Gson().toJson(prop)
    }

    @TypeConverter
    fun toProp(data: String): List<PropertiesX> {
        val listType = object : TypeToken<List<PropertiesX>>() {
        }.type
        return Gson().fromJson(data, listType)
    }


    @TypeConverter
    fun fromHolder(h: Geometry?): String? {
        val gson = Gson()
        return gson.toJson(h)
    }

    @TypeConverter
    fun toHolder(s: String?): Geometry? {
        return Gson().fromJson(s, Geometry::class.java)
    }

}