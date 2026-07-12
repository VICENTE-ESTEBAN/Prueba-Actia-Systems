package com.actia.myapplication.data.service

import com.actia.myapplication.data.repository.response.ItemDTO
import com.actia.myapplication.data.repository.response.SearchResultDTO
import retrofit2.http.*


interface RetrofitOmdbEndpoints {

    @Headers(
        "Content-Type:application/json",
        "Accept:application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "Content-Language: en-US"
    )
    @GET("/?r=json")
    suspend fun getItemsByTitle(

        @Query("apikey") apikey:String,
        @Query("s") title: String
    ): SearchResultDTO

    @Headers(
        "Content-Type:application/json",
        "Accept:application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "Content-Language: en-US"
    )
    @GET("/?r=json")
    suspend fun getItemByImdb(
        @Query("apikey") apikey:String,
        @Query("i") imdb: String
    ): ItemDTO


    @Headers(
        "Content-Type:application/json",
        "Accept:application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "Content-Language: en-US"
    )
    @GET("/?r=json")
    suspend fun getItemByTitle(

        @Query("apikey") apikey:String,
        @Query("t") title: String
    ): ItemDTO
}
