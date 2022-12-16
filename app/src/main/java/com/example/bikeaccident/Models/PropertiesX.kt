package com.example.bikeaccident.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nehody")
data class PropertiesX(
    val alkohol: String,
    val cas: Int,
    val d: Double,
    val datum: String,
    val den: Int,
    val den_v_tydnu: String,
    val druh_komun: String,
    val druh_vozidla: String,
    val e: Double,
    val globalid: String,
    val hmotna_skoda: Int,
    val hodina: Int,
    @PrimaryKey
    val id: Double,
    val join_count: Int,
    val lehce_zran_os: Int,
    val mesic: Int,
    val mesic_t: String,
    val misto_nehody: String,
    val nasledek: String,
    val nasledky: String,
    val nazev: String,
    val objectid: Int,
    val osoba: String,
    val ovlivneni_ridice: Int,
    val ozn_osoba: String,
    val pohlavi: String,
    val point_x: Double,
    val point_y: Double,
    val povetrnostni_podm: String,
    val pricina: String,
    val rok: Int,
    val rozhled: String,
    val srazka: String,
    val stav_ridic: String,
    val stav_vozovky: String,
    val target_fid: Int,
    val tezce_zran_os: Int,
    val usmrceno_os: Int,
    val vek_skupina: String,
    val viditelnost: String,
    val zavineni: String
)