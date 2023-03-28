package com.example.githubapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapi.database.Data
import com.example.githubapi.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dataUpdateViewModel: DataUpdateViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.favorite_menu -> {
                val intent = Intent(this@DetailActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.change_theme -> {
                val intent = Intent(this@DetailActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getText(R.string.details)

        // Back Button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        var user = ItemsItem("", "")

        dataUpdateViewModel = obtainViewModel(this@DetailActivity)

        val data = intent.getStringExtra("DATA")

        val detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.getUserDetails(data.toString())
        detailViewModel.detailUser.observe(this) {
            Glide.with(this@DetailActivity)
                .load(it.avatarUrl)
                .placeholder(R.drawable.profile_foreground)
                .error(R.drawable.profile_foreground)
                .into(binding.detailPhoto)
            binding.apply {
                detailName.text = it.name
                detailUsername.text = it.login
                detailFollowers.text = resources.getString(R.string.follower_detail, it.followers)
                detailFollowing.text = resources.getString(R.string.following_detail, it.following)
                detailViewModel.checkFavorite(application).observe(this@DetailActivity){available ->
                    detailFavourite.text = if(available) resources.getText(R.string.remove_fav) else resources.getText(R.string.add_to_fav)
                }
            }
            user = ItemsItem(it.avatarUrl, it.login)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.data = data.toString()

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        binding.detailFavourite.setOnClickListener {
            if(binding.detailFavourite.text.equals(resources.getText(R.string.add_to_fav))){
                val toast = Toast.makeText(this, resources.getText(R.string.add_fav_toast), Toast.LENGTH_SHORT )
                binding.detailFavourite.text = resources.getText(R.string.remove_fav)
                dataUpdateViewModel.insert(Data(0, user.login, user.avatarUrl))
                toast.show()
            }
            else {
                val toast = Toast.makeText(this, resources.getText(R.string.remove_fav_toast), Toast.LENGTH_SHORT )
                binding.detailFavourite.text = resources.getText(R.string.add_to_fav)
                dataUpdateViewModel.delete(data!!)
                toast.show()
            }
        }
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun obtainViewModel(activity: AppCompatActivity): DataUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DataUpdateViewModel::class.java]
    }
}