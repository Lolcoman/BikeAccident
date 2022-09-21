package com.example.bikeaccident.Models

data class DataResponse(
    val crs: Crs,
    val features: List<Feature>,
    val name: String,
    val type: String
)