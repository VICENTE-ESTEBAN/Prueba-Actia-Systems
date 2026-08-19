package com.actia.myapplication.data.domain.model

data class MainUiState(
    val items: ItemsState = ItemsState.Loading,
    val detail: DetailState = DetailState.Idle
)
sealed interface ItemsState {
    data object Idle : ItemsState

    data object Loading : ItemsState

    data class Success(
        val items: List<Item>,
        val selectedYear: String? = null,
        val filteredItems: List<Item> = emptyList()
    ) : ItemsState

    data class Error(
        val message: String?
    ) : ItemsState
}

sealed interface DetailState {
    data object Idle : DetailState

    data object Loading : DetailState

    data class Success(
        val item: DetailItem
    ) : DetailState

    data class Error(
        val message: String?
    ) : DetailState
}