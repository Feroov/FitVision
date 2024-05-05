package com.fitvision.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fitvision.data.AppDatabase
import com.fitvision.network.RetrofitInstance
import com.fitvision.data.ExerciseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

// ViewModel class for managing exercise data
class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises
    private val dao: ExerciseDao = AppDatabase.getDatabase(application).exerciseDao()
    private val _favoriteAdded = MutableSharedFlow<String>()
    val favoriteAdded = _favoriteAdded.asSharedFlow()

    // Expose Flow directly
    val favoritesFlow: Flow<List<FavoriteExercise>> = dao.getAllFavorites()

    init {
        fetchExercises()
    }

    // Function to fetch exercises from Retrofit
    private fun fetchExercises() {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getExercises()
            if (response.isSuccessful) {
                _exercises.postValue(response.body()?.exercises)
            }
        }
    }

    // Function to get exercise by ID
    fun getExerciseById(id: Int): Exercise? {
        return exercises.value?.find { it.id == id }
    }

    // Function to add a favorite exercise
    fun addFavorite(favoriteExercise: FavoriteExercise) {
        viewModelScope.launch {
            dao.insertFavorite(favoriteExercise)
            _favoriteAdded.emit("Added to favorites!")
        }
    }

    // Function to remove a favorite exercise
    fun removeFavorite(favoriteExercise: FavoriteExercise) {
        viewModelScope.launch {
            dao.deleteFavorite(favoriteExercise)
        }
    }
}
