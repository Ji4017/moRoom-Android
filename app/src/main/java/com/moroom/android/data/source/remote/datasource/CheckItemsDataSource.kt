package com.moroom.android.data.source.remote.datasource

import com.moroom.android.data.source.remote.model.CheckItem
import kotlinx.coroutines.flow.MutableStateFlow

interface CheckItemsDataSource {
    val checkItems: MutableStateFlow<List<CheckItem>>
    suspend fun fetchCheckItemsFromFirebase()
}