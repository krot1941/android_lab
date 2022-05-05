package com.example.loversdiary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Query("SELECT * FROM moments WHERE (:currentTime - date) % :year > 0 ORDER BY (:currentTime - date) % :year DESC LIMIT 1")
    fun getClosestMoment(
            currentTime: Long = System.currentTimeMillis(),
            year: Long = 3.154e+10.toLong()
    ): Flow<Moment>

    @Query("SELECT * FROM moments")
    fun getAllMoments(): Flow<List<Moment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moment: Moment)

    @Update
    suspend fun update(moment: Moment)

    @Delete
    suspend fun delete(moment: Moment)
}