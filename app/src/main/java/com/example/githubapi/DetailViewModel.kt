package com.example.githubapi

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.database.DataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser: LiveData<DetailResponse> = _detailUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetails(search: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().userDetail(search)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e("DetailViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailViewModel", "onFailure: ${t.message.toString()}")
                getUserDetails(search)
            }
        })
    }

    fun checkFavorite(application: Application): LiveData<Boolean> {
        val mNoteRepository = DataRepository(application)
        val usernames: LiveData<List<String>> = mNoteRepository.getAllUsername()
        val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

        usernames.observeForever { userList ->
            isFavorite.value = userList.contains(detailUser.value?.login)
        }

        return isFavorite
    }
}

