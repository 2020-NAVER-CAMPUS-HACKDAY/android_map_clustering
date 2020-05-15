package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Base Algorithm class that implements lock/unlock functionality.
 */
abstract class BaseAlgorithm<T : ClusterItem> : Algorithm<T> {
    private val mLock: ReadWriteLock = ReentrantReadWriteLock()

    override fun lock() = mLock.writeLock().lock()

    override fun unlock() = mLock.writeLock().unlock()
}