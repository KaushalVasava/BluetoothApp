package com.kaushalvasava.org.apps.bluetoothapp.di

import android.app.Application
import androidx.room.Room
import com.kaushalvasava.org.apps.bluetoothapp.db.BluetoothDatabase
import com.kaushalvasava.org.apps.bluetoothapp.repo.BluetoothRepository
import com.kaushalvasava.org.apps.bluetoothapp.repo.BluetoothRepositoryImpl
import com.kaushalvasava.org.apps.bluetoothapp.util.AppConstant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BluetoothDatabase {
        return Room.databaseBuilder(
            app,
            BluetoothDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: BluetoothDatabase): BluetoothRepository {
        return BluetoothRepositoryImpl(db.dao)
    }
}