package com.ifs21019.lostfound

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ifs21019.lostfound.data.repository.AuthRepository
import com.ifs21019.lostfound.data.repository.LocalRepository
import com.ifs21019.lostfound.data.repository.ObjectRepository
import com.ifs21019.lostfound.data.repository.UserRepository
import com.ifs21019.lostfound.presentation.login.LoginViewModel
import com.ifs21019.lostfound.presentation.main.MainViewModel
import com.ifs21019.lostfound.presentation.profile.ProfileViewModel
import com.ifs21019.lostfound.presentation.register.RegisterViewModel
import com.ifs21019.lostfound.presentation.todo.TodoViewModel
import com.ifs21019.lostfound.di.Injection


class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val objectRepository: ObjectRepository,
    private val localRepository: LocalRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel
                    .getInstance(authRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel
                    .getInstance(authRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel
                    .getInstance(authRepository, objectRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel
                    .getInstance(authRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(TodoViewModel::class.java) -> {
                TodoViewModel
                    .getInstance(objectRepository, localRepository) as T
            }
            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: " + modelClass.name
            )
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(context),
                    Injection.provideObjectRepository(context),
                    Injection.provideLocalRepository(context),
                )
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
