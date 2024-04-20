package com.ifs21019.lostfound.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ifs21019.lostfound.data.local.entity.LostFoundEntity
import com.ifs21019.lostfound.data.remote.MyResult
import com.ifs21019.lostfound.data.remote.response.LostFoundsItemResponse


class Utils {
    companion object {
        fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
            val observerWrapper = object : Observer<T> {
                override fun onChanged(value: T) {
                    observer(value)
                    if (value is MyResult.Success<*> ||
                        value is MyResult.Error
                    ) {
                        removeObserver(this)
                    }
                }
            }
            observeForever(observerWrapper)
        }

        fun entitiesToResponses(entities: List<LostFoundEntity>):
                List<LostFoundsItemResponse> {
            val responses = ArrayList<LostFoundsItemResponse>()
            entities.map {
                val response = LostFoundsItemResponse(
                    cover = it.cover ?: "",
                    updatedAt = it.updatedAt,
                    description = it.description,
                    createdAt = it.createdAt,
                    userId = it.userId,
                    id = it.id,
                    title = it.title,
                    author = it.author,
                    status = it.status,
                    isCompleted = it.isCompleted
                )
                responses.add(response)
            }
            return responses
        }
    }
}