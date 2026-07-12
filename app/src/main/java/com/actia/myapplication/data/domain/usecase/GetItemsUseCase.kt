package com.actia.myapplication.data.domain.usecase

import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.repository.network.ItemRepositoryAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetItemsUseCase(private val itemRepository: ItemRepositoryAPI) {

    fun execute(apiKey:String, title:String): Flow<Result<List<Item>>> = flow {
        emit(itemRepository.getItemsByName(apiKey, title))
    }
}