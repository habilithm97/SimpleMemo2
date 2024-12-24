package com.example.simplememo2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplememo2.R
import com.example.simplememo2.databinding.ItemMemoBinding
import com.example.simplememo2.room.Memo

class MemoAdapter(private val onItemLongClick: (Memo) -> Unit,
                  private val onItemChecked: (Memo, Boolean) -> Unit)
    : ListAdapter<Memo, MemoAdapter.MemoViewHolder>(DIFF_CALLBACK) {

    private var isMultiSelect = false

    // 상태 토글 및 갱신
    fun toggleState(isMultiSelect: Boolean) {
        this.isMultiSelect = isMultiSelect
        val updatedList = currentList.map { it.copy(isMultiSelect = isMultiSelect) }
        submitList(updatedList)
    }

    inner class MemoViewHolder(private val binding: ItemMemoBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(memo: Memo) {
                    binding.apply {
                        Log.d("MemoAdapter", "Bind 호출됨 : isMultiSelect = ${memo.isMultiSelect}")

                        this.memo = memo // xml 데이터 변수에 Memo 객체 연결
                        executePendingBindings() // 데이터를 즉시 반영
                        
                        checkBox.apply {
                            visibility = if (memo.isMultiSelect) View.VISIBLE else View.GONE
                            isChecked = false
                            setOnCheckedChangeListener { _, isChecked ->
                                onItemChecked(memo, isChecked)
                            }
                        }
                        root.setOnLongClickListener {
                            showPopupMenu(it, memo)
                            true
                        }
                    }
                }

        private fun showPopupMenu(view: View, memo: Memo) {
            PopupMenu(view.context, view).apply {
                menuInflater.inflate(R.menu.context_menu, menu)

                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            onItemLongClick(memo)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
            }

    companion object {
        // RecyclerView 성능 최적화를 위해 변경 사항만 업데이트
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}