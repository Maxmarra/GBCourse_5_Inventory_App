package com.example.inventory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: ItemRepository
): ViewModel() {

    val allItems: LiveData<List<Item>> = repository.getItemsRepo().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            repository.insertRepo(item)
        }
    }
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            repository.updateRepo(item)
        }
    }
    fun retrieveItem(id: Int): LiveData<Item> {
        return repository.getItemRepo(id).asLiveData()
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteRepo(item)
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

    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val newItem = getNewItemEntry(
            itemName,
            itemPrice,
            itemCount
        )
        insertItem(newItem)
    }

    fun isEntryValid(
        itemName: String,
        itemPrice: String,
        itemCount: String): Boolean {

        if (
            itemName.isBlank() ||
            itemPrice.isBlank() ||
            itemCount.isBlank()) {
            return false
        }
        return true
    }



}