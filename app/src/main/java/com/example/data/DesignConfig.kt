package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "design_configs")
data class DesignConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val designIndex: Int,
    val shirtColorHex: String,
    val shirtFit: String,
    val placement: String,
    val slogan: String,
    val scale: Float,
    val fade: Float,
    val timestamp: Long = System.currentTimeMillis()
)
