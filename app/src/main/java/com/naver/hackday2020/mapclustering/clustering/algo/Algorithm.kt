package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem

/**
 * Logic for computing clusters
 */
interface Algorithm<T : ClusterItem> {

    val items: Collection<T>

    var maxDistanceBetweenClusteredItems: Int

    fun addItem(item: T): Boolean

    fun addItems(items: Collection<T>): Boolean

    fun clearItems()

    fun removeItem(item: T): Boolean

    fun removeItems(items: Collection<T>): Boolean

    fun updateItem(item: T): Boolean

    fun getClusters(zoom: Double): Set<Cluster<T>>

    fun lock()

    fun unlock()
}