package com.example.inventory.data

import android.app.Application

class InventoryApplication : Application(){

    /**
     * Use lazy delegate so the instance database is lazily created
     * when you first need/access the reference (rather than when the app starts).
     * This will create the database (the physical database on the disk)
     * on the first access.*/
    val database: ItemRoomDatabase by lazy {
        ItemRoomDatabase.getDatabase(this) }
}