package com.fitvision.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "added_foods")
data class AddedFood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodId: Int,
    val name: String,
    val calories: Int
)