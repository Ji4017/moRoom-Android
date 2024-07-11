package com.moroom.android.ui.write.data.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.ui.write.data.model.CheckItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckItemsDataSourceImpl @Inject constructor(private val database: FirebaseDatabase) :
    CheckItemsDataSource {
    override val checkItems: MutableStateFlow<List<CheckItem>> = MutableStateFlow(emptyList<CheckItem>())

    override suspend fun fetchCheckItemsFromFirebase() =
        withContext(Dispatchers.IO) {
            database.getReference("CheckItem")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val checkItemList = mutableListOf<CheckItem>()
                        for (dataSnapshot in snapshot.children) {
                            val checkItem = dataSnapshot.getValue(CheckItem::class.java)
                            checkItem?.let { checkItemList.add(checkItem) }
                        }
                        checkItems.value = checkItemList
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("fetchCheckItemsFromFirebase()", error.message)
                    }
                })
        }
}