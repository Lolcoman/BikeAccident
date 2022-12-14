package com.example.bikeaccident.Models

data class DataResponse(
    val id: Int,
    val crs: Crs,
    val features: List<Feature>,
    val name: String,
    val type: String
)