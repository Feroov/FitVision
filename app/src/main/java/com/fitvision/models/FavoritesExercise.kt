package com.fitvision.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteExercise(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String
)