package com.fitvision.models


data class Food(
    val id: Int,
    val name: String,
    val calories: Int,
    val description: String,
    val imageUrl: String
)
data class FoodList(
    val foods: List<Food>
)
