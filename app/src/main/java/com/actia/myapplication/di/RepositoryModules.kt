package com.actia.myapplication.di

import com.actia.myapplication.data.repository.mappers.ItemDTOToDetailMapper
import com.actia.myapplication.data.repository.mappers.ItemsDtoToItemsMapper
import com.actia.myapplication.data.repository.network.DetailItemRepositoryAPI
import com.actia.myapplication.data.repository.network.DetailItemRepositoryAPIImpl
import com.actia.myapplication.data.repository.network.ItemRepositoryAPI
import com.actia.myapplication.data.repository.network.ItemRepositoryAPIImpl
import com.actia.myapplication.data.service.RetrofitOmdbEndpoints
import com.actia.myapplication.data.service.RetrofitOmdbProvider
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

class RepositoryModules(private val url:String) : KoinModules {
    override fun get(): List<Module> {

        return listOf(getItemsRepository(url), getDetailItemRepository(url))
    }

    private fun getItemsRepository(url:String): Module {
        return module {
            single {
                ItemRepositoryAPIImpl(
                    itemApiService = RetrofitOmdbProvider.createService(url,
                        RetrofitOmdbEndpoints::class.java),

                    itemDataMapper = ItemsDtoToItemsMapper()
                )

            } bind ItemRepositoryAPI::class
        }
    }

    private fun getDetailItemRepository(url:String): Module {
        return module{
            single {
                DetailItemRepositoryAPIImpl(
                    itemApiService =  RetrofitOmdbProvider.createService(url,
                        RetrofitOmdbEndpoints::class.java),

                    itemDataMapper = ItemDTOToDetailMapper()
                )

            } bind DetailItemRepositoryAPI::class
        }
    }

}