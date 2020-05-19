package com.naver.hackday2020.mapclustering.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterManager
import com.naver.hackday2020.mapclustering.databinding.ActivityMainBinding
import com.naver.hackday2020.mapclustering.ext.showSnack
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_category.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var naverMap: NaverMap
    private lateinit var clusterManager: ClusterManager<NaverPlaceItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity

        (map as MapFragment).getMapAsync(this)
        binding.fab.setOnClickListener { showNavView() }
        subscribeUI()

        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        clusterManager = ClusterManager(naverMap)
        viewModel.initData()
    }

    private fun subscribeUI() {
        val owner = this
        with (viewModel) {
            placeList.observe(owner, Observer { startClustering(it) })
            categoryList.observe(owner, Observer { setUpCategoryList(it) })
            errorState.observe(owner, Observer { if (it) showSnack() })
            currentCategory.observe(owner, Observer { current_category.text = it })
        }
    }

    private fun startClustering(placeList: List<Place>) {
        clusterManager.run {
            updateItems(placeList.map { NaverPlaceItem.from(it) })
            setOnClusterClickListener { onClusterClick(it) }
            setOnClusterItemClickListener { onClusterItemClick(it) }
            cluster()
        }
    }

    private fun setUpCategoryList(category: List<String>) {
        category_recycler.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CategoryRecyclerAdapter(category).apply {
                setOnItemClickListener (object : OnItemClickListener<String> {
                    override fun onItemClick(item: String) {
                        viewModel.changeCategory(item)
                        hideNavView()
                    }
                })
            }
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

    private fun showSnack() = binding.drawerLayout.showSnack(R.string.loading_error_msg)

    private fun showNavView() = binding.drawerLayout.openDrawer(nav_view)
    private fun hideNavView() = binding.drawerLayout.closeDrawer(nav_view)
}
