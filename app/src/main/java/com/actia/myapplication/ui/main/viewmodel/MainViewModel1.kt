package com.actia.myapplication.ui.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.DetailState
import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.ItemsState
import com.actia.myapplication.data.domain.model.MainUiState
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.domain.usecase.GetDetailItemByImdbUseCase
import com.actia.myapplication.data.domain.usecase.GetDetailItemByTitleUseCase
import com.actia.myapplication.data.domain.usecase.GetItemsUseCase
import com.actia.myapplication.ui.base.viewmodel.BaseViewModel
import com.actia.myapplication.util.Constants
import com.actia.myapplication.util.Constants.SHOW_ALL_YEARS
import com.actia.myapplication.util.IdlingResourceCounter.countingIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel1(
    application: Application,
    val getItemsUseCase: GetItemsUseCase,
    val getDetailItemByImdbUseCase: GetDetailItemByImdbUseCase,
    val getDetailItemByTitleUseCase: GetDetailItemByTitleUseCase) : BaseViewModel(application) {
    companion object {
        val TAG: String = MainViewModel1::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(MainUiState(
        items = ItemsState.Idle,
        detail = DetailState.Idle
    ))
    val uiState = _uiState.asStateFlow()
    //---------------USE CASES -------------------------------------------------------------------
    /*private val getItemsUseCase: GetItemsUseCase by inject()
    private val getDetailItemByImdbUseCase: GetDetailItemByImdbUseCase by inject()
    private val getDetailItemByTitleUseCase: GetDetailItemByTitleUseCase by inject()*/
    //--------------------------------------------------------------------------------------------

    fun getYears(): List<String> {
        val currentState = _uiState.value.items

        if (currentState !is ItemsState.Success) return emptyList()

        val years = currentState.items.mapNotNull { it.releaseYear }.distinct()

        return if (years.isNotEmpty()) {
            listOf(SHOW_ALL_YEARS) + years
        } else {
            emptyList()
        }
    }

    fun setSelectedYear(year: String?) {
        val currentState = _uiState.value.items

        if (currentState !is ItemsState.Success) return

        val currentStateTmp = currentState.copy()

        _uiState.update {
            it.copy(items = ItemsState.Loading)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val filteredItems = if (
                year == null || year == SHOW_ALL_YEARS
            ) {
                currentState.items
            } else {
                currentState.items.filter {
                    it.releaseYear == year
                }
            }

            _uiState.update {
                it.copy(
                    items = currentStateTmp.copy(
                        selectedYear = year,
                        filteredItems = filteredItems
                    )
                )
            }
        }
    }

    fun loadItems(title: String) {
        _uiState.update {
            it.copy(items = ItemsState.Loading)
        }

        viewModelScope.launch {
            getItemsUseCase.execute(Constants.APIKEY, title)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleGetItemsUseCase(it)
                }
        }
    }

    fun canGetDetail(data: Item?): Boolean {
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
        _uiState.update {
            it.copy(detail = DetailState.Loading)
        }

        viewModelScope.launch {
            getDetailItemByImdbUseCase.execute(Constants.APIKEY, imdb)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleGetDetailItemUseCase(it)
                }
        }
    }

    private fun getDetailItemByTitle(title:String) {
        countingIdlingResource.increment()

        _uiState.update {
            it.copy(detail = DetailState.Loading)
        }

        viewModelScope.launch {
            getDetailItemByTitleUseCase.execute(Constants.APIKEY, title)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleGetDetailItemUseCase(it)
                    countingIdlingResource.decrement()
                }
        }
    }

    private fun handleGetDetailItemUseCase(result: Result<DetailItem>) {
        when (result)
        {
            is Result.Success<DetailItem>->{
                _uiState.update {
                    it.copy(
                        detail = DetailState.Success(result.value)
                    )
                }
            }
            is Result.Failure<DetailItem>->{
                sendErrorDetail(result.throwable.message)
            }
        }
    }

    private fun handleGetItemsUseCase(result: Result<List<Item>>) {
        when (result)
        {
            is Result.Success<List<Item>>->{
                _uiState.update {
                    it.copy(
                        items = ItemsState.Success(
                            items = result.value,
                            filteredItems = result.value
                        )
                    )
                }

            }
            is Result.Failure<List<Item>>->{
                sendErrorItems(result.throwable.message)
            }
        }
    }

    private fun sendErrorDetail(error: String?) {
        error?.let { Log.e(TAG, it) }

        _uiState.update {
            it.copy(detail = DetailState.Error(error))
        }
    }

    private fun sendErrorItems(error: String?) {
        error?.let { Log.e(TAG, it) }

        _uiState.update {
            it.copy(items = ItemsState.Error(error))
        }
    }
}
