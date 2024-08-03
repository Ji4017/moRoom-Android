package com.moroom.android.presentation.write.domain.repository

import com.moroom.android.presentation.write.data.model.CheckItem
import kotlinx.coroutines.flow.Flow

interface CheckItemRepository {
    val checkItems: Flow<List<CheckItem>>
    suspend fun fetchCheckItemsFromFirebase()
    suspend fun updateCheckItem(text: String, isChecked: Boolean)
}