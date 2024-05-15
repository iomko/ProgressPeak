package com.practice.progress_peak

import android.app.Application
import androidx.room.Room
import com.practice.progress_peak.data.Database.RoomDatabase.ProgressPeakDatabase
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepository
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Spôsob vytvárania a poskytovania databázy aplikácii bolo vytvorené za pomoci video tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProgressPeakDatabase(app: Application): ProgressPeakDatabase {
        return Room.databaseBuilder(
            app,
            ProgressPeakDatabase::class.java,
            "progressPeak_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: ProgressPeakDatabase): ProgressPeakRepository {
        return ProgressPeakRepositoryImpl(db.dao)
    }
}