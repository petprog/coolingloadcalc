package com.android.petprog.coolingloadcalc.di

import android.content.Context
import androidx.room.Room
import com.android.petprog.coolingloadcalc.model.database.CalculateDataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDataBaseName() = Constants.DATABASE_NAME

    @Singleton
    @Provides
    fun provideRoomInstance(DATABASE_NAME: String,
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        CalculateDataDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCalculatedDataDao(db: CalculateDataDatabase) = db.calculatedDataDao()

}