package com.example.obstacle_game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.obstacle_game.R
import com.example.obstacle_game.data.ScoreEntry
import com.example.obstacle_game.data.ScoreRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class LocationMapFragment : Fragment() {

    private var gmap: GoogleMap? = null
    private val markers = mutableListOf<Marker>()
    private var selectedMarker: Marker? = null
    private var scores: List<ScoreEntry> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFrag = childFragmentManager.findFragmentById(R.id.innerMap) as SupportMapFragment?
            ?: SupportMapFragment.newInstance().also {
                childFragmentManager.beginTransaction()
                    .replace(R.id.innerMap, it)
                    .commit()
            }

        mapFrag.getMapAsync { map ->
            gmap = map
            reloadScores()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadScores()
    }

    fun focusOn(entry: ScoreEntry) {
        val map = gmap ?: return
        val pos = LatLng(entry.lat, entry.lng)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))

        selectedMarker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        val m = markers.firstOrNull {
            it.position.latitude == entry.lat && it.position.longitude == entry.lng
        }
        m?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        selectedMarker = m
    }

    private fun reloadScores() {
        val map = gmap ?: return
        scores = ScoreRepository.getTop10(requireContext())
        renderMarkers(map, scores)
        if (scores.isNotEmpty()) focusOn(scores[0])
    }

    private fun renderMarkers(map: GoogleMap, list: List<ScoreEntry>) {
        markers.forEach { it.remove() }
        markers.clear()

        for (e in list) {
            val pos = LatLng(e.lat, e.lng)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title("Score: ${e.score}")
                    .snippet("Distance: ${e.distance}  Coins: ${e.coins}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            if (marker != null) markers += marker
        }
    }
}
