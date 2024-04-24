package com.fitvision.models

data class Exercise(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String
)
