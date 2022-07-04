package com.example.githubuserapp

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "favorite_user")
@Parcelize
data class UserFavorite (
    @PrimaryKey
    var login: String,
    var avatar_url: String
    ): Parcelable