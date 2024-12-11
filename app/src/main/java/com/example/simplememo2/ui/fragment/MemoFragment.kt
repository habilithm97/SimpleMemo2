package com.example.simplememo2.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.simplememo2.databinding.FragmentMemoBinding
import com.example.simplememo2.room.Memo
import com.example.simplememo2.ui.activity.MainActivity
import com.example.simplememo2.viewmodel.MemoViewModel

class MemoFragment : Fragment() {
    private var _binding: FragmentMemoBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // MainActivity 툴바에 뒤로가기 버튼 활성화
        (activity as? MainActivity)?.showBackButton(true)
    }

    override fun onPause() {
        super.onPause()

        val memoStr = binding.edtMemo.text.toString()

        if (memoStr.isNotBlank()) {
            saveMemo(memoStr)
        }
    }

    private fun saveMemo(memoStr: String) {
        val memo = Memo(memoStr)
        memoViewModel.addMemo(memo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBackButton(false)
        _binding = null
    }
}