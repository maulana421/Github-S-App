package com.example.githubs.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubs.R
import com.example.githubs.adapter.GithubAdapter
import com.example.githubs.databinding.ActivityMainBinding
import com.example.githubs.model.ListUsers
import com.example.githubs.ui.detail.DetailActivity
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_AVATAR
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_USERNAME
import com.example.githubs.ui.favorite.FavoriteActivity
import com.example.githubs.ui.setting.*



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var githubAdapter: GithubAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.showLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.listUsers.observe(this) {
            setRecyler(it)
        }

    }

    private fun setRecyler(listUsers: List<ListUsers>) {
        githubAdapter = GithubAdapter(listUsers, object : GithubAdapter.OnItemCallback {
            override fun onItemClicked(list: ListUsers) {
                startActivity(Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(EXTRA_USERNAME, list.username)
                    it.putExtra(EXTRA_ID, list.id)
                    it.putExtra(EXTRA_AVATAR, list.avatar)
                })
            }
        })
        binding.rvListUsers.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_view_action).actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != null) {
                    mainViewModel.searchUser(query)
                    showLoading(true)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> Intent(this@MainActivity, FavoriteActivity::class.java).also {
                startActivity(it)
            }
            R.id.Setting -> Intent(this@MainActivity, SettingActivity::class.java).also {
                startActivity(it)
            }
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}



