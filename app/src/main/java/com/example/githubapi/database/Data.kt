package com.example.githubapi.database

import androidx.room.*
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity
@Parcelize
class Data (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "login") var login: String? = null,

    @ColumnInfo(name = "avatarUrl") var avatarUrl: String? = null,

) : Parcelable