package com.example.githubapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.database.Data
import com.example.githubapi.database.DataAdapter
import com.example.githubapi.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding

    private lateinit var adapter: DataAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.change_theme -> {
                val intent = Intent(this@FavoriteActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        title = resources.getText(R.string.favorite_title)

        // Back Button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllData().observe(this) { dataList ->
            if (dataList != null) {
                adapter.setListData(dataList)
            }
        }

        adapter = DataAdapter(DataUpdateViewModel(application))

        binding?.rvFav?.layoutManager = LinearLayoutManager(this)
        binding?.rvFav?.setHasFixedSize(true)
        binding?.rvFav?.adapter = adapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

}