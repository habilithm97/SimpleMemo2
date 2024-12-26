package com.example.simplememo2.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.simplememo2.R
import com.example.simplememo2.databinding.ActivityMainBinding
import com.example.simplememo2.ui.fragment.ListFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var isMultiSelect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

        if (savedInstanceState == null) {
            replaceFragment(ListFragment())
        }
    }

    private fun init() {
        binding.apply {
            setSupportActionBar(toolbar)
        }
    }

    // 툴바 뒤로가기 버튼 동작 처리
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // 툴바 뒤로가기 버튼 동작 정의
    override fun onBackPressed() {
        // 백스택에 프래그먼트가 존재하면 최상위 프래그먼트 제거
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else { // 백스택이 비어 있으면 기본 뒤로가기 동작 수행
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    // MemoFragment에서만 뒤로가기 버튼이 보이게 설정
    fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select -> {
                toggleMultiSelect()
                true
            }
            R.id.cancel -> {
                toggleMultiSelect()
                true
            }
            R.id.selectAll -> {
                toggleSelectAll()
                true
            }
            R.id.delete -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleMultiSelect() {
        isMultiSelect = !isMultiSelect

        val listFragment = supportFragmentManager.findFragmentById(R.id.container) as ListFragment
        listFragment.toggleState(isMultiSelect) // ListFragment로 상태 전달
        Log.d("MainActivity", "ListFragment로 상태 전달 : isMultiSelect = $isMultiSelect")

        menuVisibility(
            selectVisible = !isMultiSelect,
            cancelVisible = isMultiSelect,
            selectAllVisible = isMultiSelect,
            deleteVisible = isMultiSelect
        )
    }

    private fun menuVisibility(selectVisible: Boolean, cancelVisible: Boolean,
                               selectAllVisible: Boolean, deleteVisible: Boolean) {
        binding.toolbar.menu?.apply {
            findItem(R.id.select)?.isVisible = selectVisible
            findItem(R.id.cancel)?.isVisible = cancelVisible
            findItem(R.id.selectAll)?.isVisible = selectAllVisible
            findItem(R.id.delete)?.isVisible = deleteVisible
        }
    }

    private fun toggleSelectAll() {
        val listFragment = supportFragmentManager.findFragmentById(R.id.container) as ListFragment
        listFragment.toggleSelectAll()
    }
}