package com.example.githubs.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubs.R
import com.example.githubs.adapter.GithubAdapter
import com.example.githubs.databinding.FragmentFollowingBinding
import com.example.githubs.model.ListUsers
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_USERNAME


class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var username: String
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var githubAdapter: GithubAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        username = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_USERNAME).toString()
        binding = FragmentFollowingBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
        showLoading(true)
        followingViewModel.getFollowing(username)
        followingViewModel.listFollowing.observe(viewLifecycleOwner) {
            setRecyler(it)
            showLoading(false)
        }
    }

    private fun setRecyler(listUser: List<ListUsers>) {
        githubAdapter = GithubAdapter(listUser, object : GithubAdapter.OnItemCallback {
            override fun onItemClicked(list: ListUsers) {
            }
        })
        binding.rvFollowing.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }
}