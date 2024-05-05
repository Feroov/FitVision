package com.fitvision.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitvision.data.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

// ViewModel class for managing mood data
class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val moodDao = AppDatabase.getDatabase(application).moodDao()

    // Function to insert a new mood entry
    fun insertMood(mood: String, description: String) {
        val calories = when (mood) {
            "Awesome" -> (360..532).random()
            "Okay" -> (210..320).random()
            "Bad" -> (160..190).random()
            else -> 0
        }
        val moodEntry = MoodEntry(mood = mood, description = description, date = Date(), calories = calories)
        viewModelScope.launch {
            moodDao.insertMood(moodEntry)
        }
    }

    // Function to get all mood entries as Flow
    fun getAllMoods(): Flow<List<MoodEntry>> {
        return moodDao.getAllMoods()
    }

    // Function to delete a mood entry
    fun deleteMood(moodEntry: MoodEntry) {
        viewModelScope.launch {
            moodDao.deleteMood(moodEntry)
        }
    }
}