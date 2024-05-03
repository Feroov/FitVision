package com.fitvision.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitvision.models.AddedFood

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(food: AddedFood)

    @Query("SELECT * FROM added_foods")
    fun getAllAddedFoods(): LiveData<List<AddedFood>>

    @Query("DELETE FROM added_foods WHERE foodId = :foodId")
    suspend fun delete(foodId: Int)

    @Query("SELECT SUM(calories) FROM added_foods")
    suspend fun getAllCalories(): Int
}
