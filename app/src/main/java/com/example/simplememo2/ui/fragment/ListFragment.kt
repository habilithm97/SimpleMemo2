package com.example.simplememo2.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
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
import com.example.simplememo2.viewmodel.MemoViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memoAdapter = MemoAdapter(
            onItemLongClick = { memo ->
                showDeleteDialog(memo)
            }
        )

        binding.apply {
            rv.apply {
                adapter = memoAdapter
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }
                setHasFixedSize(true) // 아이템 크기 고정 -> 성능 최적화
            }
            fab.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, MemoFragment())
                    .addToBackStack(null) // 백스택에 추가
                    .commit()
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
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}