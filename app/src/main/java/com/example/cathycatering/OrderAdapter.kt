package com.example.cathycatering

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OrderAdapter (private var cartItems: MutableList<CartItem>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameText: TextView =itemView.findViewById(R.id.orderProductName)
        val productDescriptionText: TextView =itemView.findViewById(R.id.orderProductDescription)
        val productPriceText: TextView =itemView.findViewById(R.id.orderProductPrice)
        val productImage: ImageView =itemView.findViewById(R.id.orderProductImage)
        val quantity: TextView = itemView.findViewById(R.id.orderQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historylayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        Log.e("CATHYFIREBASE", "IN BIND $cartItem")
        holder.productNameText.text = cartItem.product.productName
        holder.productDescriptionText.text = cartItem.product.productDescription
        holder.productPriceText.text = "$${cartItem.product.productPrice.toString()}.00"
        holder.quantity.text = "Quantity: ${cartItem.quantity.toString()}"
        if(cartItem.product.productImage.isNotEmpty()){
            Picasso.get().setIndicatorsEnabled(true)
            Picasso.get()
                .load((cartItem.product.productImage))
                .into(holder.productImage)
        }
    }
    override fun getItemCount(): Int = cartItems.size
}