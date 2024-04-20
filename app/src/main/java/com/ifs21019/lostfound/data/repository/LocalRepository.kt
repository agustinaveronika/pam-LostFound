package com.ifs21019.lostfound.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ifs21019.lostfound.data.local.entity.LostFoundEntity
import com.ifs21019.lostfound.data.local.room.LostFoundDatabase
import com.ifs21019.lostfound.data.local.room.ILostFoundDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalRepository(context: Context) {
    private val mLostFoundDao: ILostFoundDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = LostFoundDatabase.getInstance(context)
        mLostFoundDao = db.lostFoundDao()
    }
    fun getAllLostFounds(): LiveData<List<LostFoundEntity>?> = mLostFoundDao.getAllLostFounds()
    fun get(lostfoundId: Int): LiveData<LostFoundEntity?> = mLostFoundDao.get(lostfoundId)
    fun insert(lostfound: LostFoundEntity) {
        executorService.execute { mLostFoundDao.insert(lostfound) }
    }
    fun delete(lostfound: LostFoundEntity) {
        executorService.execute { mLostFoundDao.delete(lostfound) }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalRepository? = null
        fun getInstance(
            context: Context
        ): LocalRepository {
            synchronized(LocalRepository::class.java) {
                INSTANCE = LocalRepository(
                    context
                )
            }
            return INSTANCE as LocalRepository
        }
    }
}