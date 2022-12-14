package com.example.bikeaccident.Models

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bikeaccident.MapsFragment
import com.example.bikeaccident.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter(context: MapsFragment) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Fragment).layoutInflater.inflate(R.layout.marker, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}