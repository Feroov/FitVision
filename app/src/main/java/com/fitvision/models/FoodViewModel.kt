package com.fitvision.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fitvision.data.AppDatabase
import com.fitvision.network.RetrofitInstance
import kotlinx.coroutines.launch

// ViewModel class for managing food data
class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitService = RetrofitInstance.api
    private val db: AppDatabase = AppDatabase.getDatabase(application)
    private val foodDao = db.foodDao()

    private val _availableFoods = MutableLiveData<List<Food>>()
    val availableFoods: LiveData<List<Food>> = _availableFoods

    // LiveData that directly reflects whats in the database
    private val _addedFoods = foodDao.getAllAddedFoods()
    val addedFoods: LiveData<List<AddedFood>> = _addedFoods

    private val _totalCaloriesConsumed = MutableLiveData<Int>()
    val totalCaloriesConsumed: LiveData<Int> = _totalCaloriesConsumed

    // Initializing by loading foods and updating calories when foods change
    init {
        loadFoods()
        updateCaloriesWhenFoodsChange()
    }

    // Function to get food by ID
    fun getFoodById(foodId: Int): Food? {
        return _availableFoods.value?.find { it.id == foodId }
    }

    // Function to load foods from Retrofit
    private fun loadFoods() {
        viewModelScope.launch {
            val response = retrofitService.getFoods()
            if (response.isSuccessful) {
                _availableFoods.postValue(response.body()?.foods ?: listOf())
            } else {
                _availableFoods.postValue(listOf())
            }
        }
    }

    // Function to add a food to the diet
    fun addFoodToDiet(food: Food) {
        viewModelScope.launch {
            foodDao.insert(AddedFood(foodId = food.id, name = food.name, calories = food.calories))
        }
    }

    // Function to remove a food from the diet
    fun removeFoodFromDiet(foodId: Int) {
        viewModelScope.launch {
            val allAddedFoods = foodDao.getAllAddedFoodsNonLive()
            val foodToRemove = allAddedFoods.firstOrNull { it.foodId == foodId }
            if (foodToRemove != null) {
                foodDao.deleteSingle(foodToRemove.id)
            }
        }
    }

    // Function to update total calories consumed when foods change
    private fun updateCaloriesWhenFoodsChange() {
        _addedFoods.observeForever { foods ->
            val totalCalories = foods.sumOf { it.calories }
            _totalCaloriesConsumed.postValue(totalCalories)
        }
    }
}