package com.example.githubapi.database

import androidx.recyclerview.widget.DiffUtil

class DataDiffCallback(private val mOldDataList: List<Data>, private val mNewDataList: List<Data>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldDataList.size
    }
    override fun getNewListSize(): Int {
        return mNewDataList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDataList[oldItemPosition].id == mNewDataList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldDataList[oldItemPosition]
        val newUser = mNewDataList[newItemPosition]
        return oldUser.login == newUser.login && oldUser.avatarUrl == newUser.avatarUrl
    }
}