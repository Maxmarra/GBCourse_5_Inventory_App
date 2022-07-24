package com.example.inventory.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.text.NumberFormat

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItemsDao().asLiveData()

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItemDao(id).asLiveData()
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insertDao(item)
        }
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.updateDao(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.deleteDao(item)
        }
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
        updateItem(updatedItem)
    }
/* ЭМУЛЯЦИЯ ПОЛУЧЕНИЯ И ДОБАВЛЕНИЯ ДАННЫХ В БАЗУ */
    //из полученных вводных данных в полях приложения
    //собираем Объект типа Item
    //и возвращаем уже готовый объект Item
    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    //имея вводные данные, через getNewItemEntry()
    //собираем, возвращаем готовый объект Item
    //и после вносим его в базу данных
    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    fun isEntryValid(itemName: String,
                     itemPrice: String,
                     itemCount: String): Boolean {
        if (
            itemName.isBlank()
            || itemPrice.isBlank()
            || itemCount.isBlank()) {
            return false
        }
        return true
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}