package com.naver.hackday2020.mapclustering.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterManager
import com.naver.hackday2020.mapclustering.databinding.ActivityMainBinding
import com.naver.hackday2020.mapclustering.ext.showSnack
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity

        (map as MapFragment).getMapAsync(this)
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)

        subscribeUI()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        viewModel.initPlaceData()
    }

    private fun subscribeUI() {
        val owner = this
        with (viewModel) {
            placeList.observe(owner, Observer { startClustering(it) })
            errorState.observe(owner, Observer { if (it) showSnack() })
        }
    }

    private fun startClustering(placeList: List<Place>) {
        ClusterManager<NaverPlaceItem>(naverMap).run {
            addItems(placeList.map { NaverPlaceItem.from(it) })
            setOnClusterClickListener { onClusterClick(it) }
            setOnClusterItemClickListener { onClusterItemClick(it) }
            cluster()
        }
    }

    private fun onClusterClick(cluster: Cluster<NaverPlaceItem>) {
        moveCameraTo(cluster.position)

        // hide bottom sheet
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun onClusterItemClick(clusterItem: NaverPlaceItem) {
        binding.place = clusterItem.place
        moveCameraTo(clusterItem.getPosition())

        // show bottom sheet
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun moveCameraTo(position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun showSnack() {
        layout_map.showSnack(R.string.loading_error_msg)
    }
}
