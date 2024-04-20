package com.ifs21019.lostfound.presentation.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21019.lostfound.ViewModelFactory
import com.ifs21019.lostfound.data.local.entity.LostFoundEntity
import com.ifs21019.lostfound.data.remote.MyResult
import com.ifs21019.lostfound.data.remote.response.DataAddObjectResponse
import com.ifs21019.lostfound.data.remote.response.LostandFoundResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundResponse
import com.ifs21019.lostfound.data.repository.LocalRepository
import com.ifs21019.lostfound.data.repository.ObjectRepository
import com.ifs21019.lostfound.presentation.ViewModelFactory


class TodoViewModel(
    private val todoRepository: TodoRepository,
    private val localTodoRepository: LocalTodoRepository
) : ViewModel() {
    fun getTodo(todoId: Int): LiveData<MyResult<DelcomTodoResponse>> {
        return todoRepository.getTodo(todoId).asLiveData()
    }
    fun postTodo(
        title: String,
        description: String,
    ): LiveData<MyResult<DataAddTodoResponse>> {
        return todoRepository.postTodo(
            title,
            description
        ).asLiveData()
    }

    fun putTodo(
        todoId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return todoRepository.putTodo(
            todoId,
            title,
            description,
            isFinished,
        ).asLiveData()
    }
    fun deleteTodo(todoId: Int): LiveData<MyResult<DelcomResponse>> {
        return todoRepository.deleteTodo(todoId).asLiveData()
    }

    fun getLocalTodos(): LiveData<List<DelcomTodoEntity>?> {
        return localTodoRepository.getAllTodos()
    }
    fun getLocalTodo(todoId: Int): LiveData<DelcomTodoEntity?> {
        return localTodoRepository.get(todoId)
    }
    fun insertLocalTodo(todo: DelcomTodoEntity) {
        localTodoRepository.insert(todo)
    }
    fun deleteLocalTodo(todo: DelcomTodoEntity) {
        localTodoRepository.delete(todo)
    }
    companion object {
        @Volatile
        private var INSTANCE: TodoViewModel? = null
        fun getInstance(
            todoRepository: TodoRepository,
            localTodoRepository: LocalTodoRepository,
        ): TodoViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = TodoViewModel(
                    todoRepository,
                    localTodoRepository
                )
            }
            return INSTANCE as TodoViewModel
        }
    }
}
