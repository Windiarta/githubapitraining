package com.example.githubapi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.databinding.ActivityMainBinding
import kotlin.collections.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<ItemsItem>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var dataUpdateViewModel: DataUpdateViewModel
    private var darkMode: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataUpdateViewModel = obtainViewModel(this@MainActivity)

        showRecyclerList(list)
        val editText = binding.searchBar
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        editText.doAfterTextChanged {
            mainViewModel.getListUser(binding.searchBar.text.toString())
        }

        mainViewModel.listUser.observe(this) { items ->
            showRecyclerList(items as ArrayList<ItemsItem>)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.change_theme -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showRecyclerList(user: ArrayList<ItemsItem>) {
        binding.rvItem.layoutManager = LinearLayoutManager(this)
        val adapter = ListAdapter(user, dataUpdateViewModel, application, this)
        binding.rvItem.adapter = adapter
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }

        })
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(user: ItemsItem) {
        showSelectedUser(user.login)
    }

    private fun showSelectedUser(user: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("DATA", user)
        startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DataUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DataUpdateViewModel::class.java]
    }

}

