package com.penguodev.memoapp.ui.payment

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.penguodev.memoapp.R
import com.penguodev.memoapp.common.BindingViewHolder
import com.penguodev.memoapp.databinding.ItemPaymentBinding

class PaymentListAdapter(private val listener: ItemClickListener) :
    ListAdapter<PaymentItem, BindingViewHolder>(object : DiffUtil.ItemCallback<PaymentItem>() {
        override fun areItemsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem.sku == newItem.sku
        }

        override fun areContentsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(parent, R.layout.item_payment).apply {
            this.bindingAs<ItemPaymentBinding>()?.textviewBuy?.setOnClickListener {
                listener.onClickBuy(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.bindingAs<ItemPaymentBinding>()?.item = getItem(position)
    }


    interface ItemClickListener {
        fun onClickBuy(item: PaymentItem)
    }
}
