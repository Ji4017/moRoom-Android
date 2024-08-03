package com.moroom.android.presentation.write.domain.model

import com.moroom.android.presentation.write.data.model.CheckItem


data class WrittenReview(
    val address: String,
    val floor: String,
    val year: String,
    val rentType: String,
    val pros: String,
    val cons: String,
    val checkItems: List<CheckItem>
)