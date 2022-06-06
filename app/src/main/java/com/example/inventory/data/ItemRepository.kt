package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun insertRepo(item: Item)

    suspend fun updateRepo(item: Item)

    suspend fun deleteRepo(item: Item)

    fun getItemRepo(id: Int): Flow<Item>

    fun getItemsRepo(): Flow<List<Item>>
}