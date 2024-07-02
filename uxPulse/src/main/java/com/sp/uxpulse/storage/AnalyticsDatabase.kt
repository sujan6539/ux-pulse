package com.sp.uxpulse.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [EventEntity::class], version = 2)
abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: AnalyticsDatabase? = null

        fun getDatabase(context: Context): AnalyticsDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AnalyticsDatabase::class.java, "analytics-database")
                .build()
    }

}