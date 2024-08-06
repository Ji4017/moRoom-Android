package com.moroom.android.domain.usecase.write

import com.moroom.android.data.source.remote.model.CheckItem
import com.moroom.android.domain.repository.CheckItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository){
    operator fun invoke(): Flow<List<CheckItem>> = repository.checkItems
}