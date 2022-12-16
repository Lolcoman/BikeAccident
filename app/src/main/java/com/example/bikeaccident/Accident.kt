package com.example.bikeaccident

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "accident_table")
data class Accident(
//    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val year: Int,
    val alc: String,
    val place: String
)