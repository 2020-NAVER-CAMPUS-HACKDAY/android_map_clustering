package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem

/**
 * Logic for computing clusters
 */
interface Algorithm<T : ClusterItem> {

    // 클러스터링 대상 아이템
    val items: Collection<T>

    var maxDistanceBetweenClusteredItems: Int

    fun addItem(item: T): Boolean

    fun addItems(items: Collection<T>): Boolean

    fun clearItems()

    fun removeItem(item: T): Boolean

    fun removeItems(items: Collection<T>): Boolean

    fun updateItem(item: T): Boolean

    // 클러스터링 로직
    fun getClusters(zoom: Double): Set<Cluster<T>>

    // 멀티 스레드 환경에서 동시에 아이템에 접근하는 것을 방지하기 위한 lock, unlock
    fun lock()

    fun unlock()
}