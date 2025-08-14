package com.example.cathycatering

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AccountAdapter (private var cartItems: MutableList<OrderList>) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productDate: TextView = itemView.findViewById(R.id.historyProductName)
        val productText: TextView = itemView.findViewById(R.id.historyProductDescription)
        val cardView: CardView= itemView.findViewById(R.id.historyCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orderlayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        Log.e("CATHYFIREBASE", "IN BIND $cartItem")
        holder.productText.text = "Click to view order details"
        holder.productDate.text = cartItem.date
        val context = holder.cardView.context
        holder.cardView.setOnClickListener {
            val intent = Intent(context, OrderHistory::class.java)
            Intent(Intent.ACTION_VIEW)
            intent.putExtra("Date",cartItem.date)
//            intent.putExtra("Name",)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = cartItems.size
}