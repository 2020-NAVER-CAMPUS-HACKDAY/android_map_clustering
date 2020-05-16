package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Base Algorithm class that implements lock/unlock functionality.
 */
abstract class BaseAlgorithm<T : ClusterItem> : Algorithm<T> {
    private val lock: ReadWriteLock = ReentrantReadWriteLock()

    override fun lock() = lock.writeLock().lock()

    override fun unlock() = lock.writeLock().unlock()
}