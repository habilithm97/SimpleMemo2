package com.example.simplememo2.repository

import com.example.simplememo2.room.Memo
import com.example.simplememo2.room.MemoDao
import kotlinx.coroutines.flow.Flow

/* Repository
 -DAO를 사용하여 Database 작업 캡슐화
 -데이터 처리 로직 분리 (데이터 가공/변환 로직을 ViewModel 대신 처리)
 -여러 데이터 소스 통합 관리 (다양한 데이터 소스를 하나로 묶어 ViewModel에 제공)
 */
class MemoRepository(private val memoDao: MemoDao) {
    suspend fun addMemo(memo: Memo) {
        memoDao.addMemo(memo)
    }
    suspend fun deleteMemo(memo: Memo) {
        memoDao.deleteMemo(memo)
    }
    suspend fun deleteMemos(memos: List<Memo>) {
        memoDao.deleteMemos(memos)
    }
    fun getAll(): Flow<List<Memo>> {
        return memoDao.getAll()
    }
}