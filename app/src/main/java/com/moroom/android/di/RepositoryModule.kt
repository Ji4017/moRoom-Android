package com.moroom.android.di

import com.moroom.android.data.repository.AccountRepositoryImpl
import com.moroom.android.data.repository.ReviewRepositoryImpl
import com.moroom.android.data.repository.CheckItemRepositoryImpl
import com.moroom.android.data.repository.CoordinatesRepositoryImpl
import com.moroom.android.domain.repository.AccountRepository
import com.moroom.android.domain.repository.CheckItemRepository
import com.moroom.android.domain.repository.CoordinatesRepository
import com.moroom.android.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCheckItemRepository(checkItemRepositoryImpl: CheckItemRepositoryImpl): CheckItemRepository

    @Binds
    @ViewModelScoped
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCoordinatesRepository(coordinatesRepositoryImpl: CoordinatesRepositoryImpl): CoordinatesRepository
}
