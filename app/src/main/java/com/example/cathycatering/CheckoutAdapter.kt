package com.example.cathycatering

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CheckoutAdapter (private var cartItems: MutableList<CartItem>, private val callback: Checkout, private val Pcallback: Product) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    val sharedViewModel = SharedViewModel()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.cartProductName)
        val productPriceTextView: TextView = itemView.findViewById(R.id.cartProductPrice)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.quantity)
        val minusBtn: FloatingActionButton = itemView.findViewById(R.id.minusBtn)
        val plusBtn: FloatingActionButton = itemView.findViewById(R.id.plusBtn)
        val delBtn: FloatingActionButton = itemView.findViewById(R.id.delBtn)
        val cardView: CardView =itemView.findViewById(R.id.historyCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cartlayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.productNameTextView.text = cartItem.product.productName
        holder.productPriceTextView.text = "$${cartItem.product.productPrice}.00"
        holder.productQuantityTextView.text = "${cartItem.quantity}"
        holder.plusBtn.setOnClickListener {
            cartItem.quantity++
            notifyItemChanged(position)
            sharedViewModel.updateItemQuantity(cartItem.product.productName, cartItem.quantity)
            sharedViewModel.saveCartItemsToFirebase()
            callback.updateTotal()
        }
        holder.minusBtn.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                notifyItemChanged(position)
                sharedViewModel.updateItemQuantity(cartItem.product.productName, cartItem.quantity)
                sharedViewModel.saveCartItemsToFirebase()
                callback.updateTotal()
            } else {
                // Remove item from cart
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                sharedViewModel.updateItemQuantity(cartItem.product.productName, cartItem.quantity)
                sharedViewModel.removeItemFromCart(cartItem.product.productName)
                callback.updateTotal()
            }
        }
        holder.delBtn.setOnClickListener{
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            sharedViewModel.updateItemQuantity(cartItem.product.productName, cartItem.quantity)
            sharedViewModel.removeItemFromCart(cartItem.product.productName)
            callback.updateTotal()
        }
    }
    override fun getItemCount(): Int = cartItems.size
}