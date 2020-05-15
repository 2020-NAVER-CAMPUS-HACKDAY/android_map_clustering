/*
 * Copyright 2016 Google Inc.
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

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.map.CameraPosition

class ScreenBasedAlgorithmAdapter<T : ClusterItem>(
    private val mAlgorithm: Algorithm<T>
) : BaseAlgorithm<T>(), ScreenBasedAlgorithm<T> {
    override val items: Collection<T>
        get() = mAlgorithm.items

    override var maxDistanceBetweenClusteredItems: Int
        get() = mAlgorithm.maxDistanceBetweenClusteredItems
        set(maxDistance) {
            mAlgorithm.maxDistanceBetweenClusteredItems = maxDistance
        }

    override fun shouldReClusterOnMapMovement() = false

    override fun addItem(item: T) = mAlgorithm.addItem(item)

    override fun addItems(items: Collection<T>) = mAlgorithm.addItems(items)

    override fun clearItems() = mAlgorithm.clearItems()

    override fun removeItem(item: T) = mAlgorithm.removeItem(item)

    override fun removeItems(items: Collection<T>) = mAlgorithm.removeItems(items)

    override fun updateItem(item: T) = mAlgorithm.updateItem(item)

    override fun getClusters(zoom: Double): Set<Cluster<T>> = mAlgorithm.getClusters(zoom)

    override fun onCameraChange(cameraPosition: CameraPosition) {
        // stub
    }
}