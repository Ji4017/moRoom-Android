package com.moroom.android.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.data.model.Review

class ResultViewModel : ViewModel() {
    private val _reviews = MutableLiveData<ArrayList<Review>>()
    val review: LiveData<ArrayList<Review>>
        get() = _reviews

    private val _reviewExists = MutableLiveData<Boolean>()
    val reviewExists: LiveData<Boolean>
        get() = _reviewExists

    fun loadReviews(searchedAddress: String) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("Address").child(searchedAddress)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    _reviews.value = extractReviewsFromSnapshot(snapshot)
                    _reviewExists.value = true
                } else _reviewExists.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(javaClass.simpleName, error.message)
            }
        })
    }

    private fun extractReviewsFromSnapshot(snapshot: DataSnapshot): ArrayList<Review> {
        val reviews = ArrayList<Review>()
        for (dataSnapshot in snapshot.children) {
            if (dataSnapshot.key == "latitude" || dataSnapshot.key == "longitude") {
                continue
            }
            val review = dataSnapshot.getValue(Review::class.java)
            review?.let { reviews.add(it) }
        }
        return reviews
    }
}