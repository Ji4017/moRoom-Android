package com.moroom.android.di

import android.content.Context
import com.moroom.android.data.source.local.AppDatabase
import com.moroom.android.data.source.local.BestReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDBModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)!!
    }

    @Provides
    fun provideBestReviewDao(appDatabase: AppDatabase): BestReviewDao {
        return appDatabase.BestReviewDao()
    }
}