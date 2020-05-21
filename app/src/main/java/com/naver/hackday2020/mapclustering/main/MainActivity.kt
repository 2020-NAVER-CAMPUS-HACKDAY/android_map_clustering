package com.naver.hackday2020.mapclustering.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterManager
import com.naver.hackday2020.mapclustering.databinding.ActivityMainBinding
import com.naver.hackday2020.mapclustering.ext.expandBottomSheet
import com.naver.hackday2020.mapclustering.ext.hideBottomSheet
import com.naver.hackday2020.mapclustering.ext.isExpanded
import com.naver.hackday2020.mapclustering.ext.showSnack
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.hackday2020.mapclustering.ui.CategoryRecyclerAdapter
import com.naver.hackday2020.mapclustering.ui.NaverPlaceItem
import com.naver.hackday2020.mapclustering.ui.PlaceRecyclerAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnItemClickListener<NaverPlaceItem> {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val placeAdapter = PlaceRecyclerAdapter()
    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var clusterManager: ClusterManager<NaverPlaceItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        subscribeUI()
        (map as MapFragment).getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        clusterManager = ClusterManager(naverMap)
        viewModel.initData()
    }

    override fun onItemClick(item: NaverPlaceItem) {
        naverMap.moveCamera(CameraUpdate.zoomTo(naverMap.maxZoom))
        moveCameraPosition(item.getPosition())
        placeAdapter.replace(item)
    }

    override fun onBackPressed() {
        if (binding.layoutBottomSheet.isExpanded()) {
            binding.layoutBottomSheet.hideBottomSheet()
        } else {
            super.onBackPressed()
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        placeAdapter.onItemClickListener = this

        binding.apply {
            lifecycleOwner = this@MainActivity
            rvPlaces.adapter = placeAdapter
            fab.setOnClickListener { showNavView() }
        }
    }

    private fun subscribeUI() {
        val owner = this
        with(viewModel) {
            placeList.observe(owner, Observer { startClustering(it) })
            categoryList.observe(owner, Observer { setUpCategoryList(it) })
            errorState.observe(owner, Observer { if (it) showSnack() })
            currentCategory.observe(owner, Observer { binding.layoutCategory.currentCategory = it })
        }
    }

    private fun startClustering(placeList: List<Place>) {
        clusterManager.run {
            updateItems(placeList.map {
                NaverPlaceItem.from(it)
            })
            setOnClusterClickListener { onClusterClick(it) }
            setOnClusterItemClickListener { onClusterItemClick(it) }
            cluster()
        }
    }

    private fun setUpCategoryList(category: List<String>) {
        binding.layoutCategory.categoryRecycler.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CategoryRecyclerAdapter(
                category
            ).apply {
                setOnItemClickListener(object : OnItemClickListener<String> {
                    override fun onItemClick(item: String) {
                        viewModel.changeCategory(item)
                        hideNavView()
                    }
                })
            }
        }
    }

    private fun onClusterClick(cluster: Cluster<NaverPlaceItem>) {
        placeAdapter.replace(cluster.items.toList())
        moveCameraPosition(cluster.position)

        // show bottom sheet
        binding.layoutBottomSheet.expandBottomSheet()
    }

    private fun onClusterItemClick(clusterItem: NaverPlaceItem) {
        placeAdapter.replace(clusterItem)
        moveCameraPosition(clusterItem.getPosition())

        // show bottom sheet
        binding.layoutBottomSheet.expandBottomSheet()
    }

    private fun moveCameraPosition(position: LatLng) {
        naverMap.moveCamera(CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing))
    }

    private fun showSnack() = binding.drawerLayout.showSnack(R.string.loading_error_msg)

    private fun showNavView() = binding.drawerLayout.openDrawer(binding.navView)

    private fun hideNavView() = binding.drawerLayout.closeDrawer(binding.navView)
}
