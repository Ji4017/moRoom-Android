package com.moroom.android.presentation.write.data.repository

import com.moroom.android.presentation.write.data.remote.CheckItemsDataSource
import com.moroom.android.presentation.write.data.model.CheckItem
import com.moroom.android.presentation.write.domain.repository.CheckItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CheckItemRepositoryImpl @Inject constructor(
    private val checkItemsDataSource: CheckItemsDataSource
) : CheckItemRepository {
    private val _checkItems = checkItemsDataSource.checkItems
    override val checkItems: Flow<List<CheckItem>> = _checkItems.asStateFlow()

    override suspend fun fetchCheckItemsFromFirebase() {
        checkItemsDataSource.fetchCheckItemsFromFirebase()
    }

    override suspend fun updateCheckItem(text: String, isChecked: Boolean) {
        _checkItems.update { items ->
            items.map { item ->
                if (item.listText == text) item.copy(isChecked = isChecked)
                else item
            }
        }
    }
}