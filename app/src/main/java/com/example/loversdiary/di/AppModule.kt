package com.example.loversdiary.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.loversdiary.data.LoversDiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: LoversDiaryDatabase.Callback)
            = Room.databaseBuilder(app, LoversDiaryDatabase::class.java, "love_diary_database")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideUserDao(db: LoversDiaryDatabase) = db.userDao()

    @Provides
    fun provideMomentDao(db: LoversDiaryDatabase) = db.momentDao()

    @Provides
    fun provideEventDao(db: LoversDiaryDatabase) = db.eventDao()

    @Provides
    fun providePhotoDao(db: LoversDiaryDatabase) = db.photoDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope