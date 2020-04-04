package com.penguodev.memoapp.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.penguodev.memoapp.R
import com.penguodev.memoapp.common.BindingViewHolder
import com.penguodev.memoapp.databinding.ItemMemoBinding
import com.penguodev.memoapp.model.MemoData
import com.penguodev.memoapp.ui.editor.EditorActivity

class HomeMemoRcvAdapter : ListAdapter<MemoData, BindingViewHolder>(object : DiffUtil.ItemCallback<MemoData>() {
    override fun areItemsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return MemoBindingViewHolder(parent).apply {
            binding.root.setOnClickListener { view ->
                view.context.startActivity(EditorActivity.createActivityIntent(view.context, getItem(adapterPosition).id))
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        (holder as? MemoBindingViewHolder)?.bindingAs<ItemMemoBinding>()?.item = getItem(position)
    }
}

class MemoBindingViewHolder(parent: ViewGroup) : BindingViewHolder(parent, R.layout.item_memo)