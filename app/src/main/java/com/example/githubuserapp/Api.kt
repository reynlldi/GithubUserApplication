package com.example.githubuserapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}