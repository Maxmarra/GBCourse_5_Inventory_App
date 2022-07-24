package com.example.inventory

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat

@BindingAdapter("formatted_price")
fun bindFormattedPrice(textView: TextView, price: Double){
    textView.text = NumberFormat.getCurrencyInstance().format(price)
}