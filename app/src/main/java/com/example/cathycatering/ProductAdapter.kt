package com.example.cathycatering

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

class ProductAdapter(val yelps: List<ProductInfo>, private val callback: ViewCart, private val Pcallback: Product): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    val sharedViewModel = SharedViewModel()

    class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout) {
        val productNameText: TextView =rootLayout.findViewById(R.id.productName)
        val productDescriptionText: TextView =rootLayout.findViewById(R.id.productDescription)
        val productPriceText: TextView =rootLayout.findViewById(R.id.productPrice)
        val productImage: ImageView =rootLayout.findViewById(R.id.productImage)
        val addCartBtn: Button = rootLayout.findViewById(R.id.addToCartBtn)
        val cardView: CardView =rootLayout.findViewById(R.id.historyCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("VH", "inside onCreateViewHolder")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View =layoutInflater.inflate(R.layout.productlayout, parent, false)
        val viewHolder=ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("VH", "inside counting the size of the array")
        return yelps.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProductInfo=yelps[position]
        holder.addCartBtn.setOnClickListener{
            sharedViewModel.addItemToCart(currentProductInfo)
            sharedViewModel.saveCartItemsToFirebase()
        }
        holder.productNameText.text= currentProductInfo.productName
        holder.productDescriptionText.text= currentProductInfo.productDescription
        holder.productPriceText.text="$${currentProductInfo.productPrice}.00"
        if(currentProductInfo.productImage.isNotEmpty()){
            Picasso.get().setIndicatorsEnabled(true)
            Picasso.get()
                .load((currentProductInfo.productImage))
                .into(holder.productImage)
        }
    }
}