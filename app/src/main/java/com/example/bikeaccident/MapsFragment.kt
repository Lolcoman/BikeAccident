package com.example.bikeaccident

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.bikeaccident.Models.PropertiesX
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    private lateinit var appDd: AccidentDatabase
    private lateinit var position: List<PropertiesX>
    private  fun getPosition(inputYear: Int,inputAlc: String): List<PropertiesX>{
        return appDd.accidentDao().getPosition(inputYear,inputAlc)
    }
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setInfoWindowAdapter(InfoWindowAdapter(this))
        googleMap.clear();
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        for (i in position.indices){
            val marker = LatLng(position[i].point_y!!,position[i].point_x!!)
            val druhKomunikace: String =
                if ((String(position[i].druh_komun!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))).isNullOrEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].druh_komun!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
            val srazka: String =
                if ((String(position[i].srazka!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))).isEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].srazka!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
            val pricina: String =
                if ((String(position[i].pricina!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))).isEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].pricina!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
            val zavineni: String =
                if ((String(position[i].zavineni!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))).isEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].zavineni!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
            val vekSkupina: String =
                if (position[i].vek_skupina.isNullOrEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].vek_skupina!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
            val nasledek: String =
                if (position[i].nasledek.isNullOrEmpty()) {
                    "Neuvedeno"
                } else {
                    String(position[i].nasledek!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                }
//            val druhKomunikace = String(position[i].druh_komun!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
//            val srazka = String(position[i].srazka!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
//            val pricina = String(position[i].pricina!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
//            val zavineni = String(position[i].zavineni!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
//            val vekSkupina = String(position[i].vek_skupina!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
//            val nasledek = String(position[i].nasledek!!.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
            googleMap.addMarker(MarkerOptions().position(marker).title(druhKomunikace).snippet("TEST").snippet(srazka + "\n" + pricina + "\n" + zavineni + "\n" + vekSkupina + "\n" + nasledek).icon(activity?.let { bitmapDescriptorFromVector(it, R.drawable.ic_baseline_directions_bike_24) }))
        }
//        val sydney = LatLng(position[0].point_y!!,position[0].point_x!!)
//        val sydneyy = LatLng(49.22375737,16.62980964)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").snippet("TEST").snippet("Guess how long this could contain?\n" +
//                "Maybe just one row?").icon(activity?.let { bitmapDescriptorFromVector(it, R.drawable.ic_baseline_directions_bike_24) }))
//        googleMap.addMarker(MarkerOptions().position(sydneyy).title("Marker in Sydney"))
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    49.195061,
                    16.606836
                ), 11.5f
            )
        )
//        val zoomLevel = 12.0f //This goes up to 21
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDd = AccidentDatabase.getDatabase(this.requireActivity())
        val args = this.arguments
        val inputYear = args?.get("rok")
        val inputAlc = args?.get("alkohol")
        if (inputAlc == "Nezjišťován")
        {
            position = getPosition(inputYear.toString().toInt(),String(inputAlc.toString().toByteArray(charset("UTF-8")), charset("ISO-8859-1")))
        }
        else {
            position = getPosition(inputYear.toString().toInt(), inputAlc.toString())
        }
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}