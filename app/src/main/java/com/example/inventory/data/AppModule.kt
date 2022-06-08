package com.example.inventory.data

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideItemDatabase(app: Application): ItemRoomDatabase {
        return Room.databaseBuilder(
            app,
            ItemRoomDatabase::class.java,
            "item_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    @Singleton
    fun provideItemRepository(db: ItemRoomDatabase):
            ItemRepository {
        return ItemRepositoryImpl(db.dao)
    }
}