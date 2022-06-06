package com.example.inventory.data

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: ItemRepository
): ViewModel() {

    val items = repository.getItemsRepo()

}