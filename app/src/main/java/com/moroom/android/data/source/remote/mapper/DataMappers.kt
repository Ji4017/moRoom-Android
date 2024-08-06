package com.moroom.android.data.source.remote.mapper

import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.data.source.remote.model.Review
import com.moroom.android.domain.model.WrittenReview
import javax.inject.Inject

class DataMappers @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun mapToDataModel(writtenReview: WrittenReview): Review {
        return Review(
            address = writtenReview.address,
            title = writtenReview.floor + " " + writtenReview.year + "년도까지 거주 " + writtenReview.rentType,
            pros = writtenReview.pros,
            cons = writtenReview.cons,
            checkedItems = writtenReview.checkItems.filter { it.isChecked }
                .joinToString(separator = "\n") { it.listText },
            idToken = auth.uid!!
        )
    }
}