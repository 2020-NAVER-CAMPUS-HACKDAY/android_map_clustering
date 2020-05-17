package com.naver.hackday2020.mapclustering.clustering

open class ClickEvent<T : ClusterItem> {
    protected var onClusterClickListener: OnClusterClickListener<T> =
        object : OnClusterClickListener<T> {
            override fun onClusterClick(cluster: Cluster<T>): Boolean {
                // TODO - camera to center
                return true
            }
        }

    protected var onClusterItemClickListener: OnClusterItemClickListener<T> =
        object : OnClusterItemClickListener<T> {
            override fun onClusterItemClick(clusterItem: T): Boolean {
                // TODO - camera to center
                return true
            }
        }

    fun setOnClusterClickListener(listener: (cluster: Cluster<T>) -> Boolean) {
        onClusterClickListener = object : OnClusterClickListener<T> {
            override fun onClusterClick(cluster: Cluster<T>): Boolean {
                return listener(cluster)
            }
        }
    }

    fun setOnClusterItemClickListener(listener: (clusterItem: T) -> Boolean) {
        onClusterItemClickListener = object : OnClusterItemClickListener<T> {
            override fun onClusterItemClick(clusterItem: T): Boolean {
                return listener(clusterItem)
            }
        }
    }

    interface OnClusterClickListener<T : ClusterItem> {
        fun onClusterClick(cluster: Cluster<T>): Boolean
    }

    interface OnClusterItemClickListener<T : ClusterItem> {
        fun onClusterItemClick(clusterItem: T): Boolean
    }
}