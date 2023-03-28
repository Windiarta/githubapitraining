package com.example.githubapi.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataRepository (application: Application){
    private val mDataDao: DataDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = DataRoomDatabase.getDatabase(application)
        mDataDao = db.dataDao()
    }
    fun getAllData(): LiveData<List<Data>> = mDataDao.getAllData()
    fun getAllUsername(): LiveData<List<String>> = mDataDao.getAllUsername()
    fun insert(data: Data) {
        executorService.execute { mDataDao.insert(data) }
    }
    fun delete(data: Data) {
        executorService.execute { mDataDao.delete(data) }
    }
    fun delete(data: String){
        executorService.execute { mDataDao.delete(data) }
    }
    fun update(data: Data) {
        executorService.execute { mDataDao.update(data) }
    }
}