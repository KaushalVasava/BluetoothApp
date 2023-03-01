package com.kaushalvasava.org.apps.bluetoothapp.db

import androidx.room.*
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDao {
    
    @Query("SELECT * FROM bluetooth_table")
    fun getAllRecords(): Flow<List<Bluetooth>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bluetooth: Bluetooth)

    @Delete
    suspend fun delete(bluetooth: Bluetooth)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Bluetooth)
}