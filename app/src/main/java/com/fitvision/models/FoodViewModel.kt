package com.fitvision.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fitvision.data.AppDatabase
import com.fitvision.network.RetrofitInstance
import androidx.lifecycle.*
import kotlinx.coroutines.launch


class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitService = RetrofitInstance.api
    private val db: AppDatabase = AppDatabase.getDatabase(application)
    private val foodDao = db.foodDao()

    private val _availableFoods = MutableLiveData<List<Food>>()
    val availableFoods: LiveData<List<Food>> = _availableFoods

    // LiveData that directly reflects what's in the database
    private val _addedFoods = foodDao.getAllAddedFoods()
    val addedFoods: LiveData<List<AddedFood>> = _addedFoods

    private val _totalCaloriesConsumed = MutableLiveData<Int>()
    val totalCaloriesConsumed: LiveData<Int> = _totalCaloriesConsumed

    init {
        loadFoods()
        updateCaloriesWhenFoodsChange()
    }

    fun getFoodById(foodId: Int): Food? {
        return _availableFoods.value?.find { it.id == foodId }
    }


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

    fun addFoodToDiet(food: Food) {
        viewModelScope.launch {
            foodDao.insert(AddedFood(foodId = food.id, name = food.name, calories = food.calories))
        }
    }

    fun removeFoodFromDiet(foodId: Int) {
        viewModelScope.launch {
            val allAddedFoods = foodDao.getAllAddedFoodsNonLive()
            val foodToRemove = allAddedFoods.firstOrNull { it.foodId == foodId }
            if (foodToRemove != null) {
                foodDao.deleteSingle(foodToRemove.id)
            }
        }
    }

    private fun updateCaloriesWhenFoodsChange() {
        _addedFoods.observeForever { foods ->
            val totalCalories = foods.sumOf { it.calories }
            _totalCaloriesConsumed.postValue(totalCalories)
        }
    }
}