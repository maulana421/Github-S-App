package com.example.githubs.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubs.R
import com.example.githubs.adapter.PagerAdapter
import com.example.githubs.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showDetail()
        setTabLayout()
        btnFavorite()
    }

    private fun showDetail() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        supportActionBar?.title = username
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        if (username != null) {
            detailViewModel.getDetailUsers(username)
        }
        detailViewModel.detailUsers.observe(this) {
            if (it != null) {
                showLoading(false)
                binding.apply {
                    tvUsernameBanner.text = it.username
                    tvUsernameProfil.text = it.username
                    tvNameDetail.text = it.name
                    tvCompany.text = it.company
                    tvLocationDetail.text = it.location
                    tvDetailRepo.text = it.repo.toString()
                    tvDetailFollowers.text = it.followers.toString()
                    tvDetailFollowing.text = it.following.toString()
                    Glide.with(ivDetailAvatar)
                        .load(it.avatar)
                        .circleCrop()
                        .into(binding.ivDetailAvatar)
                }
            }
        }
    }

    private fun btnFavorite() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUserFavorite(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleButton.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleButton.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                detailViewModel.addFavoriteUsers(id, username.toString(), avatar.toString())
                setToastSuccesAdd()
            } else {
                detailViewModel.deleteFavoriteUsers(id)
                setToastSuccesDelete()
            }
        }
    }

    private fun setTabLayout() {
        val sectionPager = PagerAdapter(this)
        binding.viewPager.adapter = sectionPager
        TabLayoutMediator(binding.tabsLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(PagerAdapter.TAB_TITLE[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shareName = binding.tvNameDetail.text.toString()
        val shareUsername = binding.tvUsernameProfil.text.toString()
        val shareCompany = binding.tvCompany.text.toString()
        val shareLocation = binding.tvLocationDetail.text.toString()
        val shareRepo = binding.tvDetailRepo.text.toString()
        val shareFollowers = binding.tvDetailFollowers.text.toString()
        val shareFollowings = binding.tvDetailFollowing.text.toString()
        val username_ = resources.getString(R.string.Username_)
        val name_ = resources.getString(R.string.Nama_)
        val company_ = resources.getString(R.string.Company_)
        val loacation_ = resources.getString(R.string.Lokasi_)
        val repository_ = resources.getString(R.string.Repositori_)
        val followers_ = resources.getString(R.string.Follower_)
        val followings_ = resources.getString(R.string.Following_)
        val shareAllData = """
            $name_ $shareName
            $username_ $shareUsername
            $company_ $shareCompany
            $loacation_ $shareLocation
            $repository_ $shareRepo
            $followers_ $shareFollowers
            $followings_ $shareFollowings
        """.trimIndent()
        val send: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareAllData)
            type = "text/plain"
        }
        val share = Intent.createChooser(send, null)
        when (item.itemId) {
            R.id.share -> startActivity(share)
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setToastSuccesAdd() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val titleToast = resources.getString(R.string.title_toast)
        val messageToast = resources.getString(R.string.message_toast_add)
        MotionToast.createColorToast(
            this,
            titleToast,
            "$username $messageToast",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.poppins)
        )
    }

    private fun setToastSuccesDelete() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val titleToast = resources.getString(R.string.title_toast)
        val messageToast = resources.getString(R.string.message_toast_delete)
        MotionToast.createColorToast(
            this,
            titleToast,
            "$username $messageToast",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.poppins)
        )
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_ID = "id"
        const val EXTRA_AVATAR = "avatar"
    }
}