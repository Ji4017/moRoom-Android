package com.moroom.android.di

import com.moroom.android.data.source.remote.datasource.AccountDataSource
import com.moroom.android.data.source.remote.datasource.AccountDataSourceImpl
import com.moroom.android.data.source.remote.datasource.AuthDataSource
import com.moroom.android.data.source.remote.datasource.AuthDataSourceImpl
import com.moroom.android.data.source.remote.datasource.CheckItemsDataSourceImpl
import com.moroom.android.data.source.remote.datasource.CheckItemsDataSource
import com.moroom.android.data.source.remote.datasource.CoordinatesDataSource
import com.moroom.android.data.source.remote.datasource.CoordinatesDataSourceImpl
import com.moroom.android.data.source.remote.datasource.ReviewDataSource
import com.moroom.android.data.source.remote.datasource.ReviewDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindAccountDataSource(accountDataSourceImpl: AccountDataSourceImpl): AccountDataSource

    @Binds
    @Singleton
    abstract fun bindCheckItemsDataSource(checkItemsDataSourceImpl: CheckItemsDataSourceImpl): CheckItemsDataSource

    @Binds
    @Singleton
    abstract fun bindCoordinatesDataSource(coordinatesDataSourceImpl: CoordinatesDataSourceImpl): CoordinatesDataSource

    @Binds
    @Singleton
    abstract fun bindReviewDataSource(reviewDataSourceImpl: ReviewDataSourceImpl): ReviewDataSource

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource
}