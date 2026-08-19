package com.actia.myapplication.ui.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.domain.usecase.GetDetailItemByImdbUseCase
import com.actia.myapplication.data.domain.usecase.GetDetailItemByTitleUseCase
import com.actia.myapplication.data.domain.usecase.GetItemsUseCase
import com.actia.myapplication.ui.base.viewmodel.BaseViewModel
import com.actia.myapplication.util.Constants
import com.actia.myapplication.util.Constants.SHOW_ALL_YEARS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    val getItemsUseCase: GetItemsUseCase,
    val getDetailItemByImdbUseCase: GetDetailItemByImdbUseCase,
    val getDetailItemByTitleUseCase: GetDetailItemByTitleUseCase
) : BaseViewModel(application) {
    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }

    val getYearsLiveData: ObservableArrayList<String> = ObservableArrayList<String>()
    val selectedYearLiveData: MutableLiveData<Int> = MutableLiveData<Int>(-1)
    private val getFullListItemsLiveData: MutableLiveData<List<Item>?> =
        MutableLiveData(emptyList())


    val getItemsLiveData: LiveData<List<Item>> = selectedYearLiveData.map { yearSelected ->
        val years = getYearsLiveData
        (if (years.isNotEmpty() && yearSelected != null && yearSelected < years.size) {
            val year = years[yearSelected]
            filterItemsByYear(year)
        } else {
            getFullListItemsLiveData.value
        }) ?: emptyList()
    }

    private val _hasErrorOnRequestiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasErrorOnRequestiveData: LiveData<Boolean> = _hasErrorOnRequestiveData


    private val _getDetailItemLiveData: MutableLiveData<DetailItem?> = MutableLiveData(null)
    val getDetailItemLiveData: LiveData<DetailItem?> = _getDetailItemLiveData

    fun loadItems(title: String) {

        getYearsLiveData.clear()
        getFullListItemsLiveData.value = emptyList()

        viewModelScope.launch {
            try {
                getItemsUseCase.execute(Constants.APIKEY, title)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        handleGetItemsUseCase(it)
                    }
            } finally {
                //IdlingResourceCounter.countingIdlingResource.decrement()
            }
        }
    }

    fun canGetDetail(data: Item?): Boolean {
        _getDetailItemLiveData.value = null
        return if (!data?.imdb.isNullOrEmpty()) {
            getDetailItemByImdb(data.imdb)
            true
        } else if (!data?.title.isNullOrEmpty()) {
            getDetailItemByTitle(data.title)
            true
        } else {
            false
        }
    }

    private fun getDetailItemByImdb(imdb: String) {
        //IdlingResourceCounter.countingIdlingResource.increment()
        viewModelScope.launch {
            try {
                getDetailItemByImdbUseCase.execute(Constants.APIKEY, imdb)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        handleGetDetailItemUseCase(it)
                    }
            } finally {
                //IdlingResourceCounter.countingIdlingResource.decrement()
            }
        }
    }

    private fun getYearsFromItem(): List<String> {
        return getFullListItemsLiveData.value?.mapNotNull {
            it.releaseYear
        }?.distinct() ?: emptyList()
    }

    private fun filterItemsByYear(year: String): List<Item> {
        return if (year == SHOW_ALL_YEARS) {
            getFullListItemsLiveData.value
        } else {
            getFullListItemsLiveData.value?.filter {
                it.releaseYear == year
            }?.toList()
        } ?: emptyList()
    }

    private fun getDetailItemByTitle(title: String) {
        //IdlingResourceCounter.countingIdlingResource.increment()

        viewModelScope.launch {
            try {
                getDetailItemByTitleUseCase.execute(Constants.APIKEY, title)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        handleGetDetailItemUseCase(it)
                    }
            } finally {
                //IdlingResourceCounter.countingIdlingResource.decrement()
            }
        }
    }

    private fun handleGetDetailItemUseCase(result: Result<DetailItem>) {
        when (result) {
            is Result.Success<DetailItem> -> {
                _getDetailItemLiveData.value = result.value
            }

            is Result.Failure<DetailItem> -> {
                sendError(result.throwable.message)
            }
        }
    }

    private fun handleGetItemsUseCase(result: Result<List<Item>>) {

        when (result) {
            is Result.Success<List<Item>> -> {

                getFullListItemsLiveData.value = result.value

                getYearsLiveData.addAll(getYearsFromItem())
                if (result.value.isNotEmpty())
                    getYearsLiveData.add(0, SHOW_ALL_YEARS)
            }

            is Result.Failure<List<Item>> -> {
                sendError(result.throwable.message)

                getYearsLiveData.clear()
            }
        }

        selectedYearLiveData.value = 0

    }

    private fun sendError(error: String?) {
        error?.let { Log.e(TAG, it) }

        _hasErrorOnRequestiveData.value = true
    }
}
