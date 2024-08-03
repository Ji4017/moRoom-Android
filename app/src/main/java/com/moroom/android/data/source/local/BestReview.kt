package com.moroom.android.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BestReview(
    @PrimaryKey val title: String = "",
    val checkedItems: String = "",
    val pros: String = "",
    val cons: String = ""
)
