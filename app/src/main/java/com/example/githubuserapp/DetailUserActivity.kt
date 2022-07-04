package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var favoriteUser: UserFavorite
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        viewModel.setUserDetail(username)
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                favoriteUser = UserFavorite(
                    login = it.login,
                    avatar_url = it.avatar_url
                )
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivProfile)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.findById(username.toString())
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0){
                        binding.toggleButton.isChecked = true
                        _isChecked = true
                    }else{
                        binding.toggleButton.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked){
                if (username != null) {
                    viewModel.insert(favoriteUser)
                }
            }else{
                viewModel.delete(favoriteUser)
            }
            binding.toggleButton.isChecked = _isChecked
        }

        val pagerAdapter = PagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = resources.getString(MainActivity.TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        viewModel.isLoading.observe(this){
                isLoading -> showLoading(isLoading)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URL = "extra_url"
    }
}