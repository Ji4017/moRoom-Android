package com.moroom.android.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.moroom.android.data.source.local.BestReview
import com.moroom.android.data.source.local.BestReviewDao
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BestReviewRepository @Inject constructor(
    private val localDataSource: BestReviewDao,
    private val firebaseDatabase: FirebaseDatabase
) {
    fun getCachedBestReviews(): List<BestReview> {
        return localDataSource.getAllReviews()
    }

    private suspend fun cacheBestReviews(bestReviews: List<BestReview>) {
        localDataSource.insertAll(bestReviews)
    }

    suspend fun loadAndCacheBestReviews(): List<BestReview> {
        val bestReviews = ArrayList<BestReview>()
        val database = firebaseDatabase.reference.child("Address").child("bestReviews")

        return try {
            val snapshot = database.get().await()
            for (data in snapshot.children) {
                val singleReview = data.getValue(BestReview::class.java)
                singleReview?.let { bestReviews.add(it) }
            }
            cacheBestReviews(bestReviews)
            bestReviews

        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.toString())
            emptyList()
        }
    }
}