package com.example.githubapi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.database.Data
import com.example.githubapi.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {
    companion object {
        const val ARG_DATA = "data"
        const val ARG_FUNC = "1"
    }

    private val list = ArrayList<ItemsItem>()
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var dataUpdateViewModel: DataUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFollowerBinding.inflate(layoutInflater)

        dataUpdateViewModel = obtainViewModel(this@FollowerFragment)

        val data = arguments?.getString(ARG_DATA)
        val func = arguments?.getInt(ARG_FUNC)

        val followerViewModel = ViewModelProvider(this)[FollowerViewModel::class.java]
        followerViewModel.getUserDetails(data.toString(), func)

        followerViewModel.listUser.observe(this) {
            showRecyclerList(it as ArrayList<ItemsItem>)
        }
        followerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        showRecyclerList(list)
        return binding.root
    }

    private fun showRecyclerList(user: ArrayList<ItemsItem>){
        binding.rvFollower.layoutManager = LinearLayoutManager(this.context)
        val adapter = ListAdapter(user, dataUpdateViewModel, requireActivity().application, this)
        binding.rvFollower.adapter = adapter
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: ItemsItem) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra("DATA", user.login)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun obtainViewModel(activity: FollowerFragment): DataUpdateViewModel {
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(activity, factory)[DataUpdateViewModel::class.java]
    }
}