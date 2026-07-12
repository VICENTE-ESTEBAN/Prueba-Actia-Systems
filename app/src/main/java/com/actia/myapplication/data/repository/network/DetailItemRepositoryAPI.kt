package com.actia.myapplication.data.repository.network

import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.Result

interface DetailItemRepositoryAPI {
    suspend fun getItemByImdb(apiKey:String, imdb:String): Result<DetailItem>
    suspend fun getItemByTitle(apiKey:String, title:String): Result<DetailItem>
}