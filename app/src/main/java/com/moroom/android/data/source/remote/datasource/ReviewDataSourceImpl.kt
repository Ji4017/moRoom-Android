package com.moroom.android.data.source.remote.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moroom.android.data.source.remote.model.Review
import com.moroom.android.presentation.result.ReviewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val crashlytics: FirebaseCrashlytics
) : ReviewDataSource {
    private val _reviewsState = MutableStateFlow<ReviewState>(ReviewState.Loading)
    override val reviewsState: StateFlow<ReviewState> = _reviewsState.asStateFlow()

    override suspend fun fetchReview(address: String) {
        val databaseReference = database.getReference("Address").child(address)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val reviewList = mutableListOf<Review>()
                    for (dataSnapshot in snapshot.children) {
                        if (dataSnapshot.key == "latitude" || dataSnapshot.key == "longitude") continue
                        val review = dataSnapshot.getValue(Review::class.java)
                        review?.let { reviewList.add(review) }
                    }
                    _reviewsState.value = ReviewState.Success(reviewList)
                } else {
                    _reviewsState.value = ReviewState.Empty
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _reviewsState.value = ReviewState.Error(error.message)
                Log.e(javaClass.simpleName, error.message)
                crashlytics.recordException(error.toException())
            }
        })
    }

    override suspend fun saveReview(review: Review): Unit =
        withContext(Dispatchers.IO) {
            try {
                database.getReference("Address")
                    .child(review.address)
                    .push()
                    .setValue(review)
            } catch (error: Exception) {
                crashlytics.setUserId(firebaseAuth.uid!!)
                crashlytics.setCustomKey("title", review.title)
                crashlytics.setCustomKey("checkedItems", review.checkedItems)
                crashlytics.setCustomKey("pros", review.pros)
                crashlytics.setCustomKey("cons", review.cons)
                crashlytics.setCustomKey("createdAt", review.createdAt)
                crashlytics.recordException(error)
            }
        }
}