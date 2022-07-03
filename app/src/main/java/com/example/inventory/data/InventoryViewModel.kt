package com.example.inventory.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    //To consume the data as a LiveData value, use the asLiveData() function
    /* TODO
        Сохрани результат запроса к базе данных всех ее элементов в
        переменную allItems
        обратись к методу базы данных getItemsDao() и получи данные как
        .asLiveData()
        не нужно использовать viewModelScope.launch() так как эти вызовы
        и так обрабатываются на другой ветке, так как они Flow
    */
    val allItems = itemDao.getItemsDao().asLiveData()
    /* TODO
        Создай метод retrieveItem() принимающий id(Int) в качестве
        параметра и возвращающий LiveData объекта Item
        данный метод извлекает из базы через метод getItemDao()
        данные об объекте с переданным id
        получи их как .asLiveData()
        не нужно использовать viewModelScope.launch() так как эти вызовы
        и так обрабатываются на другой ветке, так как они Flow */
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItemDao(id).asLiveData()
    }
    /* TODO
        Создай метод updateItem() принимающий Item в качестве
        параметра и через viewModelScope.launch вызывающий
         соответствующий метод у объекта itemDao
         метод updateDao() является suspend методом
         поэтому необходимо использование корутины viewModelScope */
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.updateDao(item)
        }
    }
    /* TODO
        Создай метод insertItem() принимающий Item в качестве
        параметра и через viewModelScope.launch вызывающий
        соответствующий метод у объекта itemDao
        метод insertDao() является suspend методом
        поэтому необходимо использование корутины viewModelScope */
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insertDao(item)
        }
    }
    /* TODO
        Создай метод isEntryValid() принимающий в качестве
        параметров itemName: String, itemPrice: String, itemCount: String
        и возращающий Boolean
        нужно проверить являются ли хоть одна из этих пустой (без значения
        при заполнении полей) с помощью метода isBlank() + ||
        и если да то сразу верни false иначе верни true */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String)
    : Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
    /* TODO
        Создай метод addNewItem() принимающий в качестве
        параметров itemName: String, itemPrice: String, itemCount: String
        Его задача создать(собрать) новый объект Item на основе введенных
        параметров приведя их в соответствие с типами класса Item
        после необходимо добавить этот объект в базу с помощью готового
        метода insertItem()*/
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {

        val newItem = Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
        insertItem(newItem)
    }

    /*TODO
         Создай метод для продажи одной единицы товара sellItem()
         - метод принимает единицу товара Item
         - сделай проверку: если у товара параметр quantityInStock больше нуля
         - то используя метод copy() создай новый объект этого товара newItem
         - с только измененным полем quantityInStock, уменьшенным на 1
         - и сразу же обнови данный объект в базе через updateItem */
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }
    /*TODO
         Создай метод isStockAvailable() принимающий объект Item
         и возвращающий Boolean
         метод возвращает результат проверки наличия
         запаса данного объекта, через > 0
         Данный метод сработает когда запас будет равен 0
         после чего кнопка Sell станет неактивна */
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    /* TODO
        Создай метод deleteItem() принимающий Item в качестве
        параметра и через viewModelScope.launch вызывающий
        соответствующий метод у объекта itemDao
        метод deleteDao() является suspend методом
        поэтому необходимо использование корутины viewModelScope */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.deleteDao(item)
        }
    }
    /* TODO
        Создай метод getUpdatedItemEntry() принимающий в качестве
        параметров itemId: Int, itemName: String,
        itemPrice: String, itemCount: String
        Его задача создать(собрать) новый объект Item на основе введенных
        параметров приведя их в соответствие с типами класса Item
        этот объект пойдет в базу, поэтому все поля нужно привести к типам
        в соответствие с полями класса Item
        после необходимо добавить этот объект в базу с помощью готового
        метода insertItem()*/
    fun updateItem(itemId: Int, itemName: String, itemPrice: String, itemCount: String
    ) {
        val updatedItem  = Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt())
        updateItem(updatedItem)
    }

}
    /*TODO
         Создай класс InventoryViewModelFactory() принимающий объект
         ItemDao и наследующийся от ViewModelProvider.Factory
         Данный класс инициализирует для нас объект InventoryViewModel
         - переопредели метод create()
         - это стандартная операция - можно ее скопировать */
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