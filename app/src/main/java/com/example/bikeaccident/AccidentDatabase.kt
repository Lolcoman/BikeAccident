package com.example.bikeaccident

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bikeaccident.Models.DataResponse
import com.example.bikeaccident.Models.Feature
import com.example.bikeaccident.Models.PropertiesX
@Database(entities = [PropertiesX::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AccidentDatabase: RoomDatabase() {
    abstract fun accidentDao(): AccidentDao


    companion object{
        private var INSTANCE: AccidentDatabase? = null

        fun getDatabase(context: Context):AccidentDatabase{
            if (INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AccidentDatabase::class.java,
                    "nehody_database"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}