package com.moroom.android.data.source.remote.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Review(
    val address: String = "",
    val title: String = "",
    val pros: String = "",
    val cons: String = "",
    val checkedItems: String = "",
    val idToken: String = "",
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
)