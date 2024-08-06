package com.moroom.android.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BestReviewDao {
    @Query("SELECT * FROM BestReview")
    fun getAllReviews(): List<BestReview>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bestReviews: List<BestReview>)
}