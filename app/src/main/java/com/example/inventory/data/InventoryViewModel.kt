package com.example.inventory.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: ItemRepository
): ViewModel() {

    val items = repository.getItemsRepo()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            repository.insertRepo(item)
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

}