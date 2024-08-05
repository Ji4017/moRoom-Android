package com.moroom.android.domain.repository

import com.moroom.android.data.source.remote.model.CheckItem
import kotlinx.coroutines.flow.Flow

interface CheckItemRepository {
    val checkItems: Flow<List<CheckItem>>
    suspend fun fetchCheckItemsFromFirebase()
    suspend fun updateCheckItem(text: String, isChecked: Boolean)
}