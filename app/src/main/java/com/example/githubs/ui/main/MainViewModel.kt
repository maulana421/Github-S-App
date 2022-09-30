package com.example.githubs.ui.mainimport android.content.ContentValues.TAGimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport com.example.githubs.api.ApiConfigimport com.example.githubs.model.ListUsersimport com.example.githubs.model.ResponseGithubimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass MainViewModel : ViewModel() {    private val _listUsers = MutableLiveData<List<ListUsers>>()    val listUsers: LiveData<List<ListUsers>> = _listUsers    private val _showLoading = MutableLiveData<Boolean>()    val showLoading: LiveData<Boolean> = _showLoading    fun searchUser(query: String) {        _showLoading.value = true        ApiConfig.getApiService().searhUsers(query)            .enqueue(object : Callback<ResponseGithub> {                override fun onResponse(                    call: Call<ResponseGithub>, response: Response<ResponseGithub>                ) {                    _showLoading.value = false                    if (response.isSuccessful) {                        _listUsers.value = response.body()?.items                    }                }                override fun onFailure(call: Call<ResponseGithub>, t: Throwable) {                    _showLoading.value = false                    Log.e(TAG, "onFailure ${t.message.toString()}")                }            })    }}