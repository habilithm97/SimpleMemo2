package com.example.simplememo2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Room은 데이터베이스 구현체를 자동 생성하므로, 추상 클래스로 선언
@Database(entities = [Memo::class], version = 2, exportSchema = true)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao

    companion object {
        /* Volatile
         -여러 스레드에서 최신 값을 읽을 수 있도록 보장
         -멀티 스레드 환경에서 일관성을 유지하도록 처리 */
        @Volatile
        private var INSTANCE: MemoDatabase? = null
        private const val DB_NAME = "memo_database"

        fun getInstance(context: Context) : MemoDatabase {
            // synchronized : 멀티 스레드 환경에서 여러 스레드가 동시에 생성하지 못하도록 동기화
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java,
                    DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance // 새로 생성한 instance를 INSTANCE에 저장
                instance
            }
        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table memo_table add column `isMultiSelect integer not null default 0")
            }
        }
    }
}