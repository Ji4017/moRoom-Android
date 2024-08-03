package com.moroom.android.presentation.write.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReviewModel(
    val address: String,
    val title: String,
    val pros: String,
    val cons: String,
    val checkedItems: String,
    val idToken: String,
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
)