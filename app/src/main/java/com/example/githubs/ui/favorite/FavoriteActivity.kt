package com.example.githubs.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubs.R
import com.example.githubs.adapter.GithubAdapter
import com.example.githubs.databinding.ActivityFavoriteBinding
import com.example.githubs.model.ListUsers
import com.example.githubs.room.entity.UsersFavorite
import com.example.githubs.ui.detail.DetailActivity
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_AVATAR
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_USERNAME
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var githubAdapter: GithubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.daftar_favorite)

        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        favoriteViewModel.getAllFavorite().observe(this) {
            if (it != null) {
                setRecyler(setFavorite(it))
            }
        }
    }

    private fun setRecyler(listUser: List<ListUsers>) {
        githubAdapter = GithubAdapter(listUser, object : GithubAdapter.OnItemCallback {
            override fun onItemClicked(list: ListUsers) {
                startActivity(Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(EXTRA_ID, list.id)
                    it.putExtra(EXTRA_USERNAME, list.username)
                    it.putExtra(EXTRA_AVATAR, list.avatar)
                })
            }
        })
        binding.rcvFavorite.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }

    private fun setFavorite(it: List<UsersFavorite>): ArrayList<ListUsers> {
        val listUsers = ArrayList<ListUsers>()
        for (user in it) {
            val users = ListUsers(
                user.id,
                user.username,
                user.avatar
            )
            listUsers.add(users)
        }
        return listUsers
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            val builder = AlertDialog.Builder(this)
            val title = resources.getString(R.string.title_alert)
            val message = resources.getString(R.string.message_alert_hapus)
            val btnPositive = resources.getString(R.string.button_positive)
            val btnNegative = resources.getString(R.string.button_negative)
            builder.setIcon(R.drawable.ic_warning)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(btnPositive) { _, _ ->
                favoriteViewModel.deleteAllFavoite()
                setToastSucces()
            }
            builder.setNegativeButton(btnNegative) { _, _ ->
            }
            builder.show()
        }
        return true
    }

    private fun setToastSucces() {
        val titleToast = resources.getString(R.string.title_toast)
        val messageToast = resources.getString(R.string.message_toast_delete)
        MotionToast.createColorToast(
            this,
            titleToast,
            messageToast,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }
}