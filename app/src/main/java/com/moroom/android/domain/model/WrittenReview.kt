package com.moroom.android.domain.model

import com.moroom.android.data.source.remote.model.CheckItem


data class WrittenReview(
    val address: String,
    val floor: String,
    val year: String,
    val rentType: String,
    val pros: String,
    val cons: String,
    val checkItems: List<CheckItem>
)