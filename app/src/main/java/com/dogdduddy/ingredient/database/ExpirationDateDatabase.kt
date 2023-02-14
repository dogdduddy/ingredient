package com.dogdduddy.ingredient.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExpirationDateData::class], version = 2)
abstract class ExpirationDateDatabase: RoomDatabase() {
    abstract fun expirationDateDao(): ExpirationDateDao

    companion object {
        private var INSTANCE: ExpirationDateDatabase? = null
        fun getInstance(context: Context): ExpirationDateDatabase? {
            if (INSTANCE == null) {
                synchronized(ExpirationDateDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ExpirationDateDatabase::class.java,
                        "expiration_date_database"
                    ).build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}