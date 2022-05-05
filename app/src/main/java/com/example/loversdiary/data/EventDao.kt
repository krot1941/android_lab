package com.example.loversdiary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT e.name AS name, COUNT(m.id) AS count FROM moments m, events e WHERE m.event_id = e.id AND m.date >= :time GROUP BY e.name")
    fun getEventsStatistic(time: Long):  Flow<List<EventStatisticQuery>>

    @Query("SELECT e.id, e.name FROM moments m, events e WHERE m.event_id=e.id")
    fun getEventsByMoments():  Flow<List<Event>>

    @Query("SELECT * FROM events")
    fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)
}