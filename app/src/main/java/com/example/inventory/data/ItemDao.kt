package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDao(item: Item)

    @Update
    suspend fun updateDao(item: Item)

    @Delete
    suspend fun deleteDao(item: Item)

    //нет suspend, так как Flow работает на background thread
    @Query("SELECT * from item WHERE id = :id")
    fun getItemDao(id: Int): Flow<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItemsDao(): Flow<List<Item>>
}