package com.example.githubapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var data: String = ""

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = FollowerFragment()
        when(position){
            0 -> {
                fragment = FollowerFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowerFragment.ARG_DATA, data)
                    putInt(FollowerFragment.ARG_FUNC, 0)
                }
            }
            1 -> {
                fragment = FollowerFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowerFragment.ARG_DATA, data)
                    putInt(FollowerFragment.ARG_FUNC, 1)
                }
            }
        }
        return fragment
    }


    override fun getItemCount(): Int {
        return 2
    }
}