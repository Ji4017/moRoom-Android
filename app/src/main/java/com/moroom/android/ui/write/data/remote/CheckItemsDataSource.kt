package com.moroom.android.ui.write.data.remote

import com.moroom.android.ui.write.data.model.CheckItem
import kotlinx.coroutines.flow.MutableStateFlow

interface CheckItemsDataSource {
    val checkItems: MutableStateFlow<List<CheckItem>>
    suspend fun fetchCheckItemsFromFirebase()
}