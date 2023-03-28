package com.example.githubapi

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubapi.database.Data
import com.example.githubapi.database.DataRepository

class DataUpdateViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: DataRepository = DataRepository(application)
    fun insert(data: Data) {
        mNoteRepository.insert(data)
    }
    fun update(data: Data) {
        mNoteRepository.update(data)
    }
    fun delete(data: Data) {
        mNoteRepository.delete(data)
    }
    fun delete(data: String) {
        mNoteRepository.delete(data)
    }

}
