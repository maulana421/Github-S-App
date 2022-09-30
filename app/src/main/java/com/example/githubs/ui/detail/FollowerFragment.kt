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
import com.example.githubs.databinding.FragmentFollowerBinding
import com.example.githubs.model.ListUsers
import com.example.githubs.ui.detail.DetailActivity.Companion.EXTRA_USERNAME


class FollowerFragment : Fragment() {
    private lateinit var binding : FragmentFollowerBinding
    private lateinit var  username : String
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var githubAdapter: GithubAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_USERNAME).toString()
        binding = FragmentFollowerBinding.bind(view)

        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]

        followersViewModel.getFollowers(username)

        showLoading(true)
        followersViewModel.listFollowers.observe(viewLifecycleOwner){
            if (it != null){
                setRecyler(it)
                showLoading(false)
            }
        }
    }

    private fun  setRecyler (list: List<ListUsers>){
        githubAdapter = GithubAdapter(list, object : GithubAdapter.OnItemCallback{
            override fun onItemClicked(list: ListUsers) {
            }
        })
        binding.rvFollowers.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}