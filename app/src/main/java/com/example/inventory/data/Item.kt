package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/*
TODO(1) создай data класс Item, который будет являтся отображением SQL таблицы в базе данных
      каждое поле этого класса - это заголовок колонки таблицы
    - присвой таблице аннотацию @Entity и имя (можно не делать,
      тогда имененем таблицы будет имя класса
    - создай 4 поля  id: Int, itemName: String,  itemPrice: Double, quantityInStock: Int
    - дай им всем аннотацию что это колонка и укажи ее имя в стиле SQL
    - сразу инициализируй id, сделай его первичным ключом с автогенерацией
    - создай extention метод для получения поля itemPrice сразу отформатированного
      под валюту страны где используется приложение в виде строки
      используй (NumberFormat.getCurrencyInstance().format(itemPrice))
*/
@Entity(tableName = "item")
data class Item(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val itemName: String,

    @ColumnInfo(name = "price")
    val itemPrice: Double,

    @ColumnInfo(name = "quantity")
    val quantityInStock: Int
)

fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)