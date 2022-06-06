package com.example.inventory.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *  Set the version as 1.
         * Whenever you change the schema of the database table,
         * you'll have to increase the version number.
    Set exportSchema to false, so as not to keep schema version history backups.
 */

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null
    }
}