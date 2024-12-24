package com.example.simplememo2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplememo2.repository.MemoRepository
import com.example.simplememo2.room.Memo
import com.example.simplememo2.room.MemoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Room Database 인스턴스 생성 시 Application Context 필요
class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private val memoRepository: MemoRepository
    val getAll: LiveData<List<Memo>>

    init { // 데이터 소스 연결, UI 관련 데이터 설정
        val memoDao = MemoDatabase.getInstance(application).memoDao()
        memoRepository = MemoRepository(memoDao)
        /* asLiveData()
         -생명주기 인식을 통해 UI에서 안전하게 데이터 관찰
         -데이터 변경 시 UI 자동 업데이트 */
        getAll = memoRepository.getAll().asLiveData()
    }

    fun addMemo(memo: Memo) {
        // 비동기적으로 IO 스레드에서 데이터 추가
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.addMemo(memo)
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.deleteMemo(memo)
        }
    }
}