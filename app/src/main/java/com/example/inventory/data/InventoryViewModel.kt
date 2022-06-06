package com.example.inventory.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insertDao(item)
        }
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