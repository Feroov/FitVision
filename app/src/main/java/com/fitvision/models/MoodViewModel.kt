package com.fitvision.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitvision.data.MoodDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val moodDao = MoodDB.getDatabase(application).moodDao()

    fun insertMood(mood: String, description: String) {
        val moodEntry = MoodEntry(mood = mood, description = description, date = Date())
        viewModelScope.launch {
            moodDao.insertMood(moodEntry)
        }
    }

    fun getAllMoods(): Flow<List<MoodEntry>> {
        return moodDao.getAllMoods()
    }

    fun deleteMood(moodEntry: MoodEntry) {
        viewModelScope.launch {
            moodDao.deleteMood(moodEntry)
        }
    }
}
