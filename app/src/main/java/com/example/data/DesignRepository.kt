package com.example.data

import kotlinx.coroutines.flow.Flow

class DesignRepository(private val dao: DesignConfigDao) {
    val allConfigs: Flow<List<DesignConfig>> = dao.getAllConfigs()

    suspend fun saveConfig(config: DesignConfig) {
        dao.insertConfig(config)
    }

    suspend fun deleteConfig(config: DesignConfig) {
        dao.deleteConfig(config)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
