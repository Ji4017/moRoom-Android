package com.moroom.android.di

import com.moroom.android.data.source.remote.datasource.AccountDataSource
import com.moroom.android.data.source.remote.datasource.AccountDataSourceImpl
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

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    @ViewModelScoped
    abstract fun bindAccountDataSource(accountDataSourceImpl: AccountDataSourceImpl): AccountDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindCheckItemsDataSource(checkItemsDataSourceImpl: CheckItemsDataSourceImpl): CheckItemsDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindCoordinatesDataSource(coordinatesDataSourceImpl: CoordinatesDataSourceImpl): CoordinatesDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindReviewDataSource(reviewDataSourceImpl: ReviewDataSourceImpl): ReviewDataSource
}