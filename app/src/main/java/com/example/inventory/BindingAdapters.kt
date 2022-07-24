package com.example.inventory

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.adapter.ItemListAdapter
import com.example.inventory.data.Item
import java.text.NumberFormat

@BindingAdapter("formatted_price")
fun bindFormattedPrice(textView: TextView, price: Double){
    textView.text = NumberFormat.getCurrencyInstance().format(price)
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Item>?) {
    val adapter = recyclerView.adapter as ItemListAdapter
    adapter.submitList(data)
}