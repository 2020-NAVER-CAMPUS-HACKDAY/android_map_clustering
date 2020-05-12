package com.naver.hackday2020

interface OnDataLoadedCallback<T> {

    fun onDataReady(data: T)

    fun onDataLoadFailed()
}