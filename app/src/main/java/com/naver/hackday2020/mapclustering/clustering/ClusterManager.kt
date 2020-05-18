package com.naver.hackday2020.mapclustering.clustering

import android.os.AsyncTask
import android.util.Log
import com.naver.hackday2020.mapclustering.clustering.algo.*
import com.naver.hackday2020.mapclustering.clustering.render.NaverMapClusterRenderer
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Groups many items on a map based on zoom level.
 */
class ClusterManager<T : ClusterItem>(private val map: NaverMap) : NaverMap.OnCameraIdleListener {

    private var clusterAlgorithm: ScreenBasedAlgorithm<T> = ScreenBasedAlgorithmAdapter(
        PreCachingAlgorithmDecorator(NonHierarchicalDistanceBasedAlgorithm())
    )
    private var previousCameraPosition: CameraPosition? = null
    private val clusterTaskLock = ReentrantReadWriteLock()
    private var clusterTask: ClusterTask = ClusterTask()
    private val naverMapRenderer = NaverMapClusterRenderer<T>(map)

    var algorithm: Algorithm<T>
        get() = clusterAlgorithm
        set(algorithm) = if (algorithm is ScreenBasedAlgorithm) {
            setAlgorithm(algorithm)
        } else {
            setAlgorithm(ScreenBasedAlgorithmAdapter(algorithm))
        }

    init {
        map.addOnCameraIdleListener(::onCameraIdle)
    }

    private fun setAlgorithm(algorithm: ScreenBasedAlgorithm<T>) {
        algorithm.lock()
        try {
            algorithm.addItems(clusterAlgorithm.items)

            clusterAlgorithm = algorithm
        } finally {
            algorithm.unlock()
        }

        if (clusterAlgorithm.shouldReClusterOnMapMovement()) {
            clusterAlgorithm.onCameraChange(map.cameraPosition)
        }

        cluster()
    }

    private fun internalLockSafe(action: (() -> Unit)) {
        clusterAlgorithm.lock()
        try {
            action.invoke()
        } catch (e: Exception) {
            Log.e(tag, "error = ${e.localizedMessage}")
        } finally {
            clusterAlgorithm.unlock()
        }
    }

    fun clearItems() {
        internalLockSafe {
            clusterAlgorithm.clearItems()
        }
    }

    fun addItems(items: Collection<T>) {
        internalLockSafe {
            clusterAlgorithm.addItems(items)
        }
    }

    fun addItem(myItem: T) {
        internalLockSafe {
            clusterAlgorithm.addItem(myItem)
        }
    }

    fun removeItem(item: T) {
        internalLockSafe {
            clusterAlgorithm.removeItem(item)
        }
    }

    /**
     * Force a re-cluster on the map. You should call this after adding, removing, updating,
     * or clearing item(s).
     */
    fun cluster() {
        clusterTaskLock.writeLock().lock()
        try {
            // Attempt to cancel the in-flight request.
            clusterTask.cancel(true)
            clusterTask = ClusterTask()
            clusterTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                map.cameraPosition.zoom
            )
        } finally {
            clusterTaskLock.writeLock().unlock()
        }
    }

    fun setOnClusterItemClickListener(onClick: (clusterItem: T) -> Unit) {
        naverMapRenderer.setOnClusterItemClickListener(onClick)
    }

    fun setOnClusterClickListener(onClick: (cluster: Cluster<T>) -> Unit) {
        naverMapRenderer.setOnClusterClickListener(onClick)
    }

    /**
     * Might re-cluster.
     */
    override fun onCameraIdle() {
        clusterAlgorithm.onCameraChange(map.cameraPosition)

        // delegate clustering to the algorithm
        if (clusterAlgorithm.shouldReClusterOnMapMovement()) {
            cluster()

            // Don't re-compute clusters if the map has just been panned/tilted/rotated.
        } else if (!isSameZoom(map.cameraPosition)) {
            previousCameraPosition = map.cameraPosition
            cluster()
        }
    }

    private fun isSameZoom(cameraPosition: CameraPosition) =
        previousCameraPosition?.zoom == cameraPosition.zoom

    /**
     * Runs the clustering algorithm in a background thread, then re-paints when results come back.
     */
    private inner class ClusterTask : AsyncTask<Double, Void, Set<Cluster<T>>>() {
        override fun doInBackground(vararg zoom: Double?): Set<Cluster<T>> {
            clusterAlgorithm.lock()
            try {
                return clusterAlgorithm.getClusters(zoom[0]!!)
            } finally {
                clusterAlgorithm.unlock()
            }
        }

        override fun onPostExecute(clusters: Set<Cluster<T>>) {
            naverMapRenderer.changeClusters(clusters)
        }
    }

    companion object {
        private val tag = ClusterManager::class.java.name
    }
}