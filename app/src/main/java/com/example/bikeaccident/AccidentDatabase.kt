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
//    abstract val accidentDao: AccidentDao
//
//
//private lateinit var INSTANCE: AccidentDatabase
//
//
//fun getDatabase(context: Context): AccidentDatabase{
//
//    synchronized(AccidentDatabase::class.java){
//        if(!::INSTANCE.isInitialized){
//            INSTANCE = Room.databaseBuilder(context,
//                AccidentDatabase::class.java,
//                "flickerImages").build()
//        }
//    }
//    return INSTANCE
//}
//
//}
//@Database(entities =  [Accident::class], version = 1)
//abstract class AccidentDatabase: RoomDatabase() {
//
//    abstract fun accidentDao(): AccidentDao
//
//    companion object{
//        @Volatile
//        private var INSTANCE: AccidentDatabase? = null
//
//        fun getDatabase(context: GraphFragment):AccidentDatabase{
//            val tempInstance = INSTANCE
//            if (tempInstance != null)
//            {
//                return tempInstance
//            }
//            synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.requireActivity().application,
//                    AccidentDatabase::class.java,
//                    "accident_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}