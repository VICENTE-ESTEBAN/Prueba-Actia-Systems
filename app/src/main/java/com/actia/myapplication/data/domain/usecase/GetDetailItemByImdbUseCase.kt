package com.actia.myapplication.data.domain.usecase

import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.repository.network.DetailItemRepositoryAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDetailItemByImdbUseCase(private val detailItemRepository: DetailItemRepositoryAPI) {

    fun execute(apiKey:String, imdb:String): Flow<Result<DetailItem>> = flow {
        emit(detailItemRepository.getItemByImdb(apiKey, imdb))
    }
}