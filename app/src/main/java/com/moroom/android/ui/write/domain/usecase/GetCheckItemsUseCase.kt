package com.moroom.android.ui.write.domain.usecase

import com.moroom.android.ui.write.data.model.CheckItem
import com.moroom.android.ui.write.domain.repository.CheckItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckItemsUseCase @Inject constructor(private val repository: CheckItemRepository){
    operator fun invoke(): Flow<List<CheckItem>> = repository.checkItems
}