package com.actia.myapplication.data.repository.network

import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.Result
import com.actia.myapplication.data.repository.mappers.helpers.Mapper
import com.actia.myapplication.data.repository.response.ItemDTO
import com.actia.myapplication.data.service.RetrofitOmdbEndpoints


class DetailItemRepositoryAPIImpl(
    private val itemApiService: RetrofitOmdbEndpoints,
    private val itemDataMapper: Mapper<ItemDTO, DetailItem>
) : DetailItemRepositoryAPI
{
    override suspend fun getItemByImdb(apiKey:String, imdb: String): Result<DetailItem> {
        return try {
            val response = itemApiService.getItemByImdb(apiKey, imdb)
            Result.Success(mapItems(response))
        } catch (e: Exception) {
            println("DetailItemRepositoryAPIImpl: $e")
            Result.Failure(e)
        }
    }

    override suspend fun getItemByTitle(apiKey:String, title: String): Result<DetailItem> {
        return try {
            val response = itemApiService.getItemByTitle(apiKey, title)
            Result.Success(mapItems(response))
        } catch (e: Exception) {
            println("DetailItemRepositoryAPIImpl: $e")
            Result.Failure(e)
        }
    }

    private fun mapItems(result: ItemDTO): DetailItem {
        return itemDataMapper.map(result)
    }

}