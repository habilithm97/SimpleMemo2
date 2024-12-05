package com.example.simplememo2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Entity
 -DB의 테이블을 나타내는 데이터 클래스
 -테이블 구조와 1:1 매핑되며, DB 관리를 단순화함
 */
@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val content: String
)