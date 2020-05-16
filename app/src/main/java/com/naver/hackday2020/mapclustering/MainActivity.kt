package com.naver.hackday2020.mapclustering

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.naver.hackday2020.mapclustering.clustering.ClusterManager
import com.naver.hackday2020.mapclustering.ext.showSnack
import com.naver.hackday2020.mapclustering.model.PlaceDataProvider
import com.naver.hackday2020.mapclustering.ui.NaverPlaceItem
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (map as MapFragment).getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        PlaceDataProvider.getAllData(
            success = {
                ClusterManager<NaverPlaceItem>(naverMap).apply {
                    addItems(NaverPlaceItem.fromList(it))
                }
            },
            failed = {
                layout_map.showSnack(R.string.loading_error_msg)
            }
        )
    }

    private fun setUpMarkers(markers: List<Marker>, naverMap: NaverMap) {
        for (marker in markers) {
            marker.map = naverMap
        }
    }
}
