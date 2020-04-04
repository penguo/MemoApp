package com.penguodev.memoapp.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.penguodev.memoapp.R
import com.penguodev.memoapp.common.BindingViewHolder
import com.penguodev.memoapp.databinding.ItemMemoBinding
import com.penguodev.memoapp.model.MemoData

class HomeMemoRcvAdapter :
    ListAdapter<MemoData, BindingViewHolder>(object : DiffUtil.ItemCallback<MemoData>() {
        override fun areItemsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return MemoBindingViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        (holder as? MemoBindingViewHolder)?.bind(getItem(position))
    }

}

class MemoBindingViewHolder(parent: ViewGroup) : BindingViewHolder(parent, R.layout.item_memo) {

    fun bind(item: MemoData) {
        bindingAs<ItemMemoBinding>()?.item = item
    }

}