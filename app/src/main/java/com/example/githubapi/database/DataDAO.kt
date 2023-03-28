package com.example.githubapi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Data)
    @Update
    fun update(data: Data)
    @Delete
    fun delete(data: Data)
    @Query("DELETE from data WHERE login = :data")
    fun delete(data: String)
    @Query("SELECT * from data ORDER BY id ASC")
    fun getAllData(): LiveData<List<Data>>
    @Query("SELECT login from data ORDER BY id ASC")
    fun getAllUsername(): LiveData<List<String>>
}