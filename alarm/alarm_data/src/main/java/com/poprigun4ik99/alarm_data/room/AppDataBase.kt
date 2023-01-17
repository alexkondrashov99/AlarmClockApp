package com.poprigun4ik99.alarm_data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poprigun4ik99.alarm_data.room.datasources.AlarmDao
import com.poprigun4ik99.alarm_data.room.model.AlarmRecordRoom

@Database(
    entities = [
        AlarmRecordRoom::class
    ],
    version = 6
)

abstract class AppDataBase : RoomDatabase() {

    abstract val alarmDao: AlarmDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                return instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "alarm_app_room_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also {
                    instance = it
                }
            }
        }
    }
}