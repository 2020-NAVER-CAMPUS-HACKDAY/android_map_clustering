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
    private val algorithm: Algorithm<T>
) : BaseAlgorithm<T>() {
    private val cache = LruCache<Int, Set<Cluster<T>>>(10)
    private val cacheLock = ReentrantReadWriteLock()

    override val items: Collection<T>
        get() = algorithm.items

    override var maxDistanceBetweenClusteredItems: Int
        get() = algorithm.maxDistanceBetweenClusteredItems
        set(maxDistance) {
            algorithm.maxDistanceBetweenClusteredItems = maxDistance
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
        clearCacheInternal { algorithm.addItem(item) }

    override fun addItems(items: Collection<T>): Boolean =
        clearCacheInternal { algorithm.addItems(items) }

    override fun removeItem(item: T): Boolean =
        clearCacheInternal { algorithm.removeItem(item) }

    override fun removeItems(items: Collection<T>): Boolean =
        clearCacheInternal { algorithm.removeItems(items) }

    override fun updateItem(item: T): Boolean =
        clearCacheInternal { algorithm.updateItem(item) }

    override fun clearItems() {
        algorithm.clearItems()
        clearCache()
    }

    private fun clearCache() {
        cache.evictAll()
    }

    override fun getClusters(zoom: Double): Set<Cluster<T>> {
        val discreteZoom = zoom.toInt()
        val results = getClustersInternal(discreteZoom)
        if (cache.get(discreteZoom + 1) == null) {
            Thread(PrecacheRunnable(discreteZoom + 1)).start()
        }
        if (cache.get(discreteZoom - 1) == null) {
            Thread(PrecacheRunnable(discreteZoom - 1)).start()
        }
        return results
    }

    private fun getClustersInternal(discreteZoom: Int): Set<Cluster<T>> {
        var results: Set<Cluster<T>>?
        cacheLock.readLock().lock()
        results = cache.get(discreteZoom)
        cacheLock.readLock().unlock()

        // cache 되어 있지 않으면 cluster 계산
        if (results == null) {
            cacheLock.writeLock().lock()
            results = cache.get(discreteZoom)
            if (results == null) {
                results = algorithm.getClusters(discreteZoom.toDouble())
                cache.put(discreteZoom, results)
            }
            cacheLock.writeLock().unlock()
        }
        return results
    }

    private inner class PrecacheRunnable(private val zoom: Int) : Runnable {

        override fun run() {
            try {
                // Wait between 500 - 1000 ms.
                Thread.sleep((Math.random() * 500 + 500).toLong())
            } catch (e: InterruptedException) {
                // ignore. keep going.
            }

            getClustersInternal(zoom)
        }
    }
}