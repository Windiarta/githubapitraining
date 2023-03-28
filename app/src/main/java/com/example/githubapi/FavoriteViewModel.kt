package com.example.githubapi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.database.Data
import com.example.githubapi.database.DataRepository

class FavoriteViewModel (application: Application) : ViewModel()  {
    private val mNoteRepository: DataRepository = DataRepository(application)
    fun getAllData(): LiveData<List<Data>> = mNoteRepository.getAllData()
}