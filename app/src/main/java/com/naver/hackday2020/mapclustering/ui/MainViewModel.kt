package com.naver.hackday2020.mapclustering.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.hackday2020.mapclustering.model.PlaceDataProvider
import com.naver.hackday2020.mapclustering.util.StringUtil

class MainViewModel : ViewModel() {
    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>> = _categoryList

    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> = _errorState

    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String> = _currentCategory

    fun changeCategory(category: String) {
        _currentCategory.value = category
    }

    fun initData() {
        initPlaceData()
        initCategoryData()
    }

    private fun initPlaceData() {
        PlaceDataProvider.getAllData(
                success = { _placeList.value = it.places },
                failed = { _errorState.value = true }
        )
    }

    private fun initCategoryData() {
        PlaceDataProvider.getCategoryData(
                success = { _categoryList.value = getCategoryList(it) },
                failed = { _errorState.value = true }
        )
    }

    private fun getCategoryList(categoryMap: HashMap<String, Int>): List<String> {
        val categories = mutableListOf<String>()
        val totalDataCount =  categoryMap.values.sum()
        val categoryAll = StringUtil.getString(R.string.category_all)
        categories.add("$categoryAll ($totalDataCount)")
        _currentCategory.value = categories[0]

        val categoryList = categoryMap
                .toSortedMap()
                .toList()
                .sortedByDescending { (_, count) -> count }
                .map { (category, number) -> "$category ($number)" }
                .toList()

        categories.addAll(categoryList)
        return categories
    }

}