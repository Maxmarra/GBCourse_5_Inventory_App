package com.example.inventory.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Item::class],
    version = 2,
    exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract val dao: ItemDao

}

















































