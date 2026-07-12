package com.actia.myapplication.data.repository.network

import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.repository.mappers.helpers.Mapper
import com.actia.myapplication.data.repository.response.SearchResultDTO
import com.actia.myapplication.data.service.RetrofitOmdbEndpoints


class ItemRepositoryAPIImpl(
    private val itemApiService: RetrofitOmdbEndpoints,
    private val itemDataMapper: Mapper<SearchResultDTO, List<Item>>
) : ItemRepositoryAPI
{
    override suspend fun getItemsByName(apiKey:String, title: String): Result<List<Item>> {
        return try {
            val response = itemApiService.getItemsByTitle(apiKey, title)
            Result.Success(mapItems(response))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    private fun mapItems(result: SearchResultDTO): List<Item> {
        return itemDataMapper.map(result)
    }

}