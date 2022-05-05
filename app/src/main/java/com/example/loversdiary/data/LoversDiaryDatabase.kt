package com.example.loversdiary.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.loversdiary.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [User::class, Moment::class, Photo::class, Event::class], version = 1)
@TypeConverters(Converters::class)
abstract class LoversDiaryDatabase: RoomDatabase() {

    abstract fun momentDao(): MomentDao
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun photoDao(): PhotoDao

    class Callback @Inject constructor(
        private val database: Provider<LoversDiaryDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val momentDao = database.get().momentDao()
            val eventDao = database.get().eventDao()
            val userDao = database.get().userDao()
            val photoDao = database.get().photoDao()

            applicationScope.launch {
                eventDao.insert(Event(
                        name = "Свидание",
                ))
                eventDao.insert(Event(
                        name = "Кино",
                ))
                eventDao.insert(Event(
                        name = "Театр",
                ))
                eventDao.insert(Event(
                        name = "Путешествия",
                ))
                eventDao.insert(Event(
                        name = "Сюрприз",
                ))
                eventDao.insert(Event(
                        name = "Цветы",
                ))
                momentDao.insert(Moment(
                        place = "Ё-бар",
                        note = "Я увидел тебя танцующей под Минимал Л.Джея, это было что то с чем то...",
                        event_id = 1,
                        date = LocalDateTime.of(
                                2021,
                                4,
                                8,
                                0,
                                0,
                                0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                ))
                momentDao.insert(Moment(
                        place = "Cuba Libre",
                        note = "Я тебя споил и затанцевал. И тут ты меня поцеловала в первый раз...",
                        event_id = 5,
                        date = LocalDateTime.of(
                                2021,
                                4,
                                9,
                                0,
                                0,
                                0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                ))
            }
        }
    }
}