package com.fitvision.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitvision.models.FavoriteExercise

@Database(entities = [FavoriteExercise::class], version = 1, exportSchema = false)
abstract class FavoritesDB : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritesDB? = null

        fun getDatabase(context: Context): FavoritesDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDB::class.java,
                    "exercise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}