package com.example.githubuserapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserFavorite::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase?{
            if (INSTANCE == null){
                synchronized(UserDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "user_database").build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}