package com.fitvision.models;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fitvision.network.RetrofitInstance

class ExerciseViewModel : ViewModel() {
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

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

}
