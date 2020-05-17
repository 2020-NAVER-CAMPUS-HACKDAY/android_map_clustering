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

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.geometry.LatLng

/**
 * A cluster whose center is determined upon creation.
 */
class StaticCluster<T : ClusterItem>(override val position: LatLng) : Cluster<T> {
    private val itemList = ArrayList<T>()

    override val items: Collection<T>
        get() = itemList

    override val size: Int
        get() = itemList.size

    fun add(t: T) = itemList.add(t)

    fun remove(t: T) = itemList.remove(t)

    fun isCluster() = size > 1

    fun isPlaceItem() = size == 1

    override fun toString(): String {
        return "StaticCluster{" +
                "mCenter= $position" +
                ", itemList.size=${itemList.size}" +
                '}'
    }

    override fun hashCode(): Int {
        return position.hashCode() + itemList.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is StaticCluster<*>) {
            false
        } else other.position == position && other.itemList == itemList
    }
}