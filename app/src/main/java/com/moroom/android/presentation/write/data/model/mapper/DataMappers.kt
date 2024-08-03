package com.moroom.android.presentation.write.data.model.mapper

import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.presentation.write.data.model.ReviewModel
import com.moroom.android.presentation.write.domain.model.WrittenReview
import javax.inject.Inject

class DataMappers @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun mapToDataModel(writtenReview: WrittenReview): ReviewModel {
        return ReviewModel(
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