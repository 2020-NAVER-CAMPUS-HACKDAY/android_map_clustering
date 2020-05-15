/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naver.hackday2020.mapclustering.clustering.algo

import androidx.collection.LruCache
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Optimistically fetch clusters for adjacent zoom levels, caching them as necessary.
 */
class PreCachingAlgorithmDecorator<T : ClusterItem>(
    private val mAlgorithm: Algorithm<T>
) : BaseAlgorithm<T>() {
    private val mCache = LruCache<Int, Set<Cluster<T>>>(5)
    private val mCacheLock = ReentrantReadWriteLock()

    override val items: Collection<T>
        get() = mAlgorithm.items

    override var maxDistanceBetweenClusteredItems: Int
        get() = mAlgorithm.maxDistanceBetweenClusteredItems
        set(maxDistance) {
            mAlgorithm.maxDistanceBetweenClusteredItems = maxDistance
            clearCache()
        }

    private fun clearCacheInternal(action:() -> Boolean): Boolean {
        val result = action()
        if (result) {
            clearCache()
        }
        return result
    }

    override fun addItem(item: T): Boolean =
        clearCacheInternal { mAlgorithm.addItem(item) }

    override fun addItems(items: Collection<T>): Boolean =
        clearCacheInternal { mAlgorithm.addItems(items) }

    override fun removeItem(item: T): Boolean =
        clearCacheInternal { mAlgorithm.removeItem(item) }

    override fun removeItems(items: Collection<T>): Boolean =
        clearCacheInternal { mAlgorithm.removeItems(items) }

    override fun updateItem(item: T): Boolean =
        clearCacheInternal { mAlgorithm.updateItem(item) }

    override fun clearItems() {
        mAlgorithm.clearItems()
        clearCache()
    }

    private fun clearCache() {
        mCache.evictAll()
    }

    override fun getClusters(zoom: Double): Set<Cluster<T>> {
        val discreteZoom = zoom.toInt()
        val results = getClustersInternal(discreteZoom)
        if (mCache.get(discreteZoom + 1) == null) {
            Thread(PrecacheRunnable(discreteZoom + 1)).start()
        }
        if (mCache.get(discreteZoom - 1) == null) {
            Thread(PrecacheRunnable(discreteZoom - 1)).start()
        }
        return results
    }

    private fun getClustersInternal(discreteZoom: Int): Set<Cluster<T>> {
        var results: Set<Cluster<T>>?
        mCacheLock.readLock().lock()
        results = mCache.get(discreteZoom)
        mCacheLock.readLock().unlock()

        // cache 되어 있지 않으면 cluster 계산
        if (results == null) {
            mCacheLock.writeLock().lock()
            results = mCache.get(discreteZoom)
            if (results == null) {
                results = mAlgorithm.getClusters(discreteZoom.toDouble())
                mCache.put(discreteZoom, results)
            }
            mCacheLock.writeLock().unlock()
        }
        return results
    }

    private inner class PrecacheRunnable(private val mZoom: Int) : Runnable {

        override fun run() {
            try {
                // Wait between 500 - 1000 ms.
                Thread.sleep((Math.random() * 500 + 500).toLong())
            } catch (e: InterruptedException) {
                // ignore. keep going.
            }

            getClustersInternal(mZoom)
        }
    }
}