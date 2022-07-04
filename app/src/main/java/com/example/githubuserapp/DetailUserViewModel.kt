package com.example.githubuserapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(usename: String?){
        _isLoading.value = true
        usename?.let {
            ApiConfig.apiInstance
                .getUserDetail(it)
                .enqueue(object: Callback<DetailUserResponse>{
                    override fun onResponse(
                        call: Call<DetailUserResponse>,
                        response: Response<DetailUserResponse>
                    ) { _isLoading.value = false
                        if (response.isSuccessful){
                            user.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                        _isLoading.value = false
                        t.message?.let { Log.d("Failure", it) }
                    }

                })
        }
    }

    fun getUserDetail(): LiveData<DetailUserResponse>{
        return user
    }

    fun insert(favoriteUser: UserFavorite){
        executorService.execute { userDao?.insert(favoriteUser) }
    }

    fun findById(login: String) = userDao?.findById(login)

    fun delete(favoriteUser: UserFavorite){
        executorService.execute {userDao?.delete(favoriteUser)}
    }
}