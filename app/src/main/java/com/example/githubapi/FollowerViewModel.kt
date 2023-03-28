package com.example.githubapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel: ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetails(search: String, function: Int?){
        _isLoading.value = true
        var client = ApiConfig.getApiService().userFollower(search)
        when (function){
            0 -> {client = ApiConfig.getApiService().userFollower(search)}
            1 -> {client = ApiConfig.getApiService().userFollowing(search)}
        }
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listUser.value = response.body()
                } else {
                    _isLoading.value = false
                    Log.e("FollowerViewModel", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e("FollowerViewModel", "onFailure: ${t.message.toString()}")
                getUserDetails(search, function)
            }
        })
    }
}

