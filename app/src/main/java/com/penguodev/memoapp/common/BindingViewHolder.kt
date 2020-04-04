package com.penguodev.memoapp.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindingViewHolder(open val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    constructor(
        viewGroup: ViewGroup,
        resId: Int
    ) : this(
        DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            resId,
            viewGroup,
            false
        )
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDataBinding> bindingAs(): T? {
        return binding as? T
    }
}