package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DesignConfigDao {
    @Query("SELECT * FROM design_configs ORDER BY timestamp DESC")
    fun getAllConfigs(): Flow<List<DesignConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: DesignConfig)

    @Delete
    suspend fun deleteConfig(config: DesignConfig)

    @Query("DELETE FROM design_configs")
    suspend fun clearAll()
}
