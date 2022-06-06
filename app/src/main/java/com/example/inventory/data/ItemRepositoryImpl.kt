package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class ItemRepositoryImpl(private val daoImpl:ItemDao): ItemRepository{

    override suspend fun insertRepo(item: Item) {
        daoImpl.insertDao(item)
    }

    override suspend fun updateRepo(item: Item) {
        daoImpl.updateDao(item)
    }

    override suspend fun deleteRepo(item: Item) {
        daoImpl.deleteDao(item)
    }

    override fun getItemRepo(id: Int): Flow<Item> {
        return daoImpl.getItemDao(id)
    }

    override fun getItemsRepo(): Flow<List<Item>> {
        return daoImpl.getItemsDao()
    }
}