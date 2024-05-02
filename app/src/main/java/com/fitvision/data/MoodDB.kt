package com.fitvision.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitvision.models.MoodEntry
import com.fitvision.util.Converters

@Database(entities = [MoodEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoodDB : RoomDatabase() {
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: MoodDB? = null

        fun getDatabase(context: Context): MoodDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDB::class.java,
                    "fitvision_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
