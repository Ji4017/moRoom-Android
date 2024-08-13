package com.moroom.android.data.repository

import com.moroom.android.data.source.remote.datasource.CheckItemsDataSource
import com.moroom.android.data.source.remote.model.CheckItem
import com.moroom.android.domain.repository.CheckItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CheckItemRepositoryImpl @Inject constructor(
    private val checkItemsDataSource: CheckItemsDataSource
) : CheckItemRepository {

    private val _checkItems: MutableStateFlow<List<CheckItem>> = checkItemsDataSource.checkItems
    override val checkItems: Flow<List<CheckItem>> = _checkItems

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