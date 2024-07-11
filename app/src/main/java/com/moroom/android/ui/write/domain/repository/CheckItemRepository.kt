package com.moroom.android.ui.write.domain.repository

import com.moroom.android.ui.write.data.model.CheckItem
import kotlinx.coroutines.flow.Flow

interface CheckItemRepository {
    val checkItems: Flow<List<CheckItem>>
    suspend fun fetchCheckItemsFromFirebase()
    suspend fun updateCheckItem(text: String, isChecked: Boolean)
}