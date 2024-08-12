package com.moroom.android.data.source.remote.model

import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.domain.model.WrittenReview
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

fun WrittenReview.toDataModel(auth: FirebaseAuth): Review = Review(
    address = this.address,
    title = this.floor + " " + this.year + "년도까지 거주 " + this.rentType,
    pros = this.pros,
    cons = this.cons,
    checkedItems = this.checkItems.filter { it.isChecked }.joinToString(separator = "\n") { it.listText },
    idToken = auth.uid!!
)