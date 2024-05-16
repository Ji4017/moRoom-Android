package com.moroom.android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BestReviewViewModel : ViewModel() {
    private val _bestReviews = MutableLiveData<ArrayList<Review>>()

    val bestReviews: LiveData<ArrayList<Review>>
        get() = _bestReviews

    init {
        loadBestReviews()
    }

    private fun loadBestReviews() {
        val fetchedBestReviews = ArrayList<Review>()
        val database = Firebase.database.reference
        database.child("Address").child("bestReviews").get()
            .addOnSuccessListener { snapshot ->
                for (data in snapshot.children) {
                    val singleReview = data.getValue(Review::class.java)
                    singleReview?.let { fetchedBestReviews.add(it) }
                }
                _bestReviews.value = fetchedBestReviews
            }
            .addOnFailureListener {
                Log.e(javaClass.simpleName, it.toString())
            }
    }
}