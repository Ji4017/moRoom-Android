package com.moroom.android.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BestReview::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun BestReviewDao(): BestReviewDao

    companion object {
        private var instance: AppDatabase? = null



        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "bestReview-database"
                    )
                        .build()
                }
            }
            return instance
        }
    }
}