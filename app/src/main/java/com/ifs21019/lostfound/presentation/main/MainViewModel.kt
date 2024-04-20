package com.ifs21019.lostfound.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ifs21019.lostfound.presentation.ViewModelFactory
import com.ifs21019.lostfound.data.pref.UserModel
import com.ifs21019.lostfound.data.remote.MyResult
import com.ifs21019.lostfound.data.remote.response.LostFoundsResponse
import com.ifs21019.lostfound.data.remote.response.LostandFoundResponse
import com.ifs21019.lostfound.data.repository.AuthRepository
import com.ifs21019.lostfound.data.repository.ObjectRepository
import kotlinx.coroutines.launch
class MainViewModel(
    private val authRepository: AuthRepository,
    private val objectRepository: ObjectRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
    fun getObjects(): LiveData<MyResult<LostFoundsResponse>> {
        return objectRepository.getObjects(null).asLiveData()
    }
    fun putObject(
        objectId: Int,
        title: String,
        description: String,
        status: String,
        isFinished: Boolean,
    ): LiveData<MyResult<LostandFoundResponse>> {
        return objectRepository.putObject(
            objectId,
            title,
            description,
            status,
            isFinished,
        ).asLiveData()
    }
    companion object {
        @Volatile
        private var INSTANCE: MainViewModel? = null
        fun getInstance(
            authRepository: AuthRepository,
            objectRepository: ObjectRepository
        ): MainViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = MainViewModel(
                    authRepository,
                    objectRepository
                )
            }
            return INSTANCE as MainViewModel
        }
    }
}