package com.naver.hackday2020.mapclustering

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterManager
import com.naver.hackday2020.mapclustering.databinding.ActivityMainBinding
import com.naver.hackday2020.mapclustering.ext.showSnack
import com.naver.hackday2020.mapclustering.model.PlaceDataProvider
import com.naver.hackday2020.mapclustering.ui.NaverPlaceItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        (map as MapFragment).getMapAsync(this)
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        PlaceDataProvider.getAllData(
            success = { placeList ->
                ClusterManager<NaverPlaceItem>(naverMap).run {
                    addItems(NaverPlaceItem.fromList(placeList))
                    setOnClusterClickListener { onClusterClick(it) }
                    setOnClusterItemClickListener { onClusterItemClick(it) }
                    cluster()
                }
            },
            failed = {
                layout_map.showSnack(R.string.loading_error_msg)
            }
        )
    }

    private fun onClusterClick(cluster: Cluster<NaverPlaceItem>) {
        moveCameraTo(cluster.position)
    }

    private fun onClusterItemClick(clusterItem: NaverPlaceItem) {
        binding.place = clusterItem.place
        moveCameraTo(clusterItem.getPosition())
        showBottomSheet()
    }

    private fun moveCameraTo(position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun showBottomSheet() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
