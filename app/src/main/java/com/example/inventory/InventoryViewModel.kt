package com.example.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventory.data.ItemDao

class InventoryViewModel(
    private val itemDao: ItemDao
    ) : ViewModel() {

    }

class InventoryViewModelFactory(
    private val itemDao: ItemDao) : ViewModelProvider.Factory {
}