package com.naver.hackday2020.mapclustering.clustering

import android.os.AsyncTask
import android.util.Log
import com.naver.hackday2020.mapclustering.clustering.algo.*
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Groups many items on a map based on zoom level.
 */
class ClusterManager<T : ClusterItem>(
    private val map: NaverMap
) : NaverMap.OnCameraIdleListener {

    private var mAlgorithm: ScreenBasedAlgorithm<T> = ScreenBasedAlgorithmAdapter(
        PreCachingAlgorithmDecorator(NonHierarchicalDistanceBasedAlgorithm())
    )
    private var previousCameraPosition: CameraPosition? = null
    private val mClusterTaskLock = ReentrantReadWriteLock()
    private var mClusterTask: ClusterTask = ClusterTask()

    var algorithm: Algorithm<T>?
        get() = mAlgorithm
        set(algorithm) = if (algorithm is ScreenBasedAlgorithm) {
            setAlgorithm(algorithm)
        } else {
            setAlgorithm(ScreenBasedAlgorithmAdapter(algorithm!!))
        }

    init {
        map.addOnCameraIdleListener(::onCameraIdle)
    }

    private fun setAlgorithm(algorithm: ScreenBasedAlgorithm<T>) {
        algorithm.lock()
        try {
            algorithm.addItems(mAlgorithm.items)

            mAlgorithm = algorithm
        } finally {
            algorithm.unlock()
        }

        if (mAlgorithm.shouldReClusterOnMapMovement()) {
            mAlgorithm.onCameraChange(map.cameraPosition)
        }

        cluster()
    }

    private fun internalLockSafe(action: (() -> Unit)) {
        mAlgorithm.lock()
        try {
            action.invoke()
        } catch (e: Exception) {
            Log.e(tag, "error = ${e.localizedMessage}")
        } finally {
            mAlgorithm.unlock()
        }
    }

    fun clearItems() {
        internalLockSafe {
            mAlgorithm.clearItems()
        }
    }

    fun addItems(items: Collection<T>) {
        internalLockSafe {
            mAlgorithm.addItems(items)
        }
    }

    fun addItem(myItem: T) {
        internalLockSafe {
            mAlgorithm.addItem(myItem)
        }
    }

    fun removeItem(item: T) {
        internalLockSafe {
            mAlgorithm.removeItem(item)
        }
    }

    /**
     * Force a re-cluster on the map. You should call this after adding, removing, updating,
     * or clearing item(s).
     */
    fun cluster() {
        mClusterTaskLock.writeLock().lock()
        try {
            // Attempt to cancel the in-flight request.
            mClusterTask.cancel(true)
            mClusterTask = ClusterTask()
            mClusterTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                map.cameraPosition.zoom
            )
        } finally {
            mClusterTaskLock.writeLock().unlock()
        }
    }

    /**
     * Might re-cluster.
     */
    override fun onCameraIdle() {
        mAlgorithm.onCameraChange(map.cameraPosition)

        // delegate clustering to the algorithm
        if (mAlgorithm.shouldReClusterOnMapMovement()) {
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
            mAlgorithm.lock()
            try {
                return mAlgorithm.getClusters(zoom[0]!!)
            } finally {
                mAlgorithm.unlock()
            }
        }

        override fun onPostExecute(clusters: Set<Cluster<T>>) {
            // Renderer 으로 결과 clusters 전달
        }
    }

    companion object {
        private val tag = ClusterManager::class.java.name
    }
}