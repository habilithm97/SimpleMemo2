package com.example.simplememo2.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplememo2.R
import com.example.simplememo2.adapter.MemoAdapter
import com.example.simplememo2.databinding.FragmentListBinding
import com.example.simplememo2.room.Memo
import com.example.simplememo2.ui.activity.MainActivity
import com.example.simplememo2.viewmodel.MemoViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels()
    private lateinit var memoAdapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // 어댑터와 뷰가 결합되기 전에 데이터 준비 및 설정
        memoAdapter = MemoAdapter(
            onItemLongClick = { memo ->
                showDeleteDialog(memo)
            },
            onItemChecked = { memo, isChecked ->

            }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rv.apply {
                adapter = memoAdapter
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }
                setHasFixedSize(true) // 아이템 크기 고정 -> 성능 최적화
            }
            fabAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MemoFragment())
                    .addToBackStack(null) // 백스택에 추가
                    .commit()
            }
            fabDelete.setOnClickListener {
                val checkedMemos = mutableListOf<Memo>() // 체크된 메모 리스트
                val listSize = memoAdapter.currentList.size // 전체 리스트 크기
                // 전체 리스트를 순회하며 체크된 메모 찾기
                for (index in 0 until listSize) {
                    val memo = memoAdapter.currentList[index]
                    // 체크된 메모만 리스트에 객체 추가
                    if (memo.isChecked) {
                        checkedMemos.add(memo)
                    }
                }
                if (checkedMemos.isNotEmpty()) {
                    showDeleteDialog2(checkedMemos)
                }
            }
            memoViewModel.getAll.observe(viewLifecycleOwner) {
                // 새로운 데이터 리스트를 어댑터에 제출
                memoAdapter.submitList(it) {
                    val itemCount = memoAdapter.itemCount
                    if (itemCount > 0) {
                        rv.smoothScrollToPosition(itemCount - 1) // 마지막 아이템 위치로 자동 스크롤
                    }
                }
            }
            val isMultiSelect = (requireActivity() as MainActivity).isMultiSelect
            memoAdapter.toggleState(isMultiSelect)
            Log.d("ListFragment", "MemoAdapter로 초기 상태 전달 : isMultiSelect = $isMultiSelect")
        }
    }

    fun toggleState(isMultiSelect: Boolean) {
        memoAdapter.toggleState(isMultiSelect)
        binding.fabDelete.visibility = if (isMultiSelect) View.VISIBLE else View.GONE
        Log.d("ListFragment", "MemoAdapter로 상태 전달 : isMultiSelect = $isMultiSelect")
    }

    fun toggleSelectAll() {
        val isSelectAll = memoAdapter.isSelectAll()
        memoAdapter.toggleSelectAll(!isSelectAll) // 현재 상태의 반대값으로 체크 상태 토글
    }

    private fun showDeleteDialog(memo: Memo) {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제")
            .setMessage("선택한 메모를 삭제할까요 ?")
            .setPositiveButton("삭제") { dialog, _ ->
                memoViewModel.deleteMemo(memo)
                dialog.dismiss()
            }
            .setNegativeButton("취소",null)
            .show()
    }

    private fun showDeleteDialog2(memos: List<Memo>) {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제")
            .setMessage("선택한 메모를 삭제할까요 ?")
            .setPositiveButton("삭제") { dialog, _ ->
                memoViewModel.deleteMemos(memos)
                dialog.dismiss()
            }
            .setNegativeButton("취소",null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}