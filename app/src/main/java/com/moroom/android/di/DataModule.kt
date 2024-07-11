package com.moroom.android.di

import com.moroom.android.ui.write.data.remote.AccountDataSource
import com.moroom.android.ui.write.data.remote.AccountDataSourceImpl
import com.moroom.android.ui.write.data.remote.CheckItemsDataSourceImpl
import com.moroom.android.ui.write.data.remote.CheckItemsDataSource
import com.moroom.android.ui.write.data.remote.CoordinatesDataSource
import com.moroom.android.ui.write.data.remote.CoordinatesDataSourceImpl
import com.moroom.android.ui.write.data.remote.ReviewDataSource
import com.moroom.android.ui.write.data.remote.ReviewDataSourceImpl
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