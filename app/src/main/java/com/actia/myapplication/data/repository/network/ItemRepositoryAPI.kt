package com.actia.myapplication.data.repository.network

import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.Result

interface ItemRepositoryAPI {
    suspend fun getItemsByName(apiKey:String, title:String): Result<List<Item>>
}