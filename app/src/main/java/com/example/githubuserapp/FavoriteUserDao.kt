package com.example.githubuserapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser : UserFavorite)

    @Query("SELECT *FROM favorite_user")
    fun loadAll(): LiveData<List<UserFavorite>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.login = :login")
    fun findById(login: String) : Int

    @Delete
    fun delete(favoriteUser: UserFavorite)
}