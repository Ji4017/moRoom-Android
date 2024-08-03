package com.moroom.android.presentation.write.domain.usecase

import com.moroom.android.presentation.write.data.model.CheckItem
import com.moroom.android.presentation.write.domain.repository.CheckItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository){
    operator fun invoke(): Flow<List<CheckItem>> = repository.checkItems
}