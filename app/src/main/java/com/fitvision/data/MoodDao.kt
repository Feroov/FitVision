package com.fitvision.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.fitvision.models.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert
    suspend fun insertMood(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Delete
    suspend fun deleteMood(moodEntry: MoodEntry)
}
