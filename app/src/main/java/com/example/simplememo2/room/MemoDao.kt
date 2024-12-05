package com.example.simplememo2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/* DAO(Data Access Object)
 -DB 작업을 정의하는 인터페이스
 -데이터 작업과 SQL 쿼리를 간단하게 정의
 */
@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMemo(memo: Memo)

    // Flow : 비동기 데이터 스트림 -> 순차적 데이터 업데이트
    @Query("select * from memo order by id")
    fun getAll(): Flow<List<Memo>>
}