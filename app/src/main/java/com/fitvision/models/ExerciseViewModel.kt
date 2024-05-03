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

    private fun fetchExercises() {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getExercises()
            if (response.isSuccessful) {
                _exercises.postValue(response.body()?.exercises)
            }
        }
    }

    fun getExerciseById(id: Int): Exercise? {
        return exercises.value?.find { it.id == id }
    }

    fun addFavorite(favoriteExercise: FavoriteExercise) {
        viewModelScope.launch {
            dao.insertFavorite(favoriteExercise)
            _favoriteAdded.emit("Added to favorites!") // Emitting a success message
        }
    }

    fun removeFavorite(favoriteExercise: FavoriteExercise) {
        viewModelScope.launch {
            dao.deleteFavorite(favoriteExercise)
        }
    }
}
