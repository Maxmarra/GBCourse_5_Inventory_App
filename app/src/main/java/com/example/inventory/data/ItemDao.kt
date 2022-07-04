package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
/*
TODO(2)
 - создай interface ItemDao, данный интерфейс будет обращаться к базе данных
   все методы данного интерфейса будут реализованы Room под капотом
 - у нас будет 5 методов - для вставки, обновления, удаления, выбора всех элементов из базы
   и выбора одного элемента по его id
 - используй аннотации @Insert @Update @Delete @Query
 - для @Insert добавь стратегию при конфликте вставки (игнорить)
 - три метода сделай suspend а два возвращающих Flow
 - метод выбора всех элементов запроси с сортировкой (ORDER BY) по имени
   сверху вниз

*/
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