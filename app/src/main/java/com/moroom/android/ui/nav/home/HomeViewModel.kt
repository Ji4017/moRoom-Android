package com.moroom.android.ui.nav.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.moroom.android.data.local.BestReview

class HomeViewModel : ViewModel() {
    private val _bestReviews = MutableLiveData<List<BestReview>>()

    val bestReviews: LiveData<List<BestReview>>
        get() = _bestReviews

    init {
        loadBestReviews()
    }

    private fun loadBestReviews() {
        val fetchedBestReviews = ArrayList<BestReview>()
        val database = Firebase.database.reference
        database.child("Address").child("bestReviews").get()
            .addOnSuccessListener { snapshot ->
                for (data in snapshot.children) {
                    val singleReview = data.getValue(BestReview::class.java)
                    singleReview?.let { fetchedBestReviews.add(it) }
                }
                _bestReviews.value = fetchedBestReviews
            }
            .addOnFailureListener {
                Log.e(javaClass.simpleName, it.toString())
            }
    }
}