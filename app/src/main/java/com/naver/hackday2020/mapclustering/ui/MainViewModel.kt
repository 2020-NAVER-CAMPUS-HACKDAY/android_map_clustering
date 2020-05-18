package com.naver.hackday2020.mapclustering.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.hackday2020.mapclustering.model.PlaceDataProvider

class MainViewModel : ViewModel() {
    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private val _errorState = MutableLiveData<Boolean>(false)
    val errorState: LiveData<Boolean> = _errorState

    fun initPlaceData() {
        PlaceDataProvider.getAllData(
            success = { _placeList.value = it.places },
            failed = { _errorState.value = true }
        )
    }

}