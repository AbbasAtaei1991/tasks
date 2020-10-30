package com.ataei.abbas.karam.di

import android.content.Context
import com.ataei.abbas.karam.data.model.AppDatabase
import com.ataei.abbas.karam.data.model.JobDao
import com.ataei.abbas.karam.jobs.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDataBase(appContext)

    @Singleton
    @Provides
    fun provideJobDao(db: AppDatabase) = db.jobDao()

    @Singleton
    @Provides
    fun provideRepository(localDataSource: JobDao) = JobRepository(localDataSource)
}