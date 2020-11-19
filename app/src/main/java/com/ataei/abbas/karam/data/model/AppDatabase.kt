package com.ataei.abbas.karam.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataei.abbas.karam.data.dao.DailyDao
import com.ataei.abbas.karam.data.dao.DayDao
import com.ataei.abbas.karam.data.dao.JobDao

@Database(entities = [Day::class, Job::class, Daily::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobDao() : JobDao
    abstract fun dayDao() : DayDao
    abstract fun dailyDao() : DailyDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDataBase(context: Context) : AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it }}

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "appDb")
                .fallbackToDestructiveMigration()
                .build()
    }
}