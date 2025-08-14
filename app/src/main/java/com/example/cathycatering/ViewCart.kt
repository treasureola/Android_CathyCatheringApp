package com.example.cathycatering

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewCart : AppCompatActivity() {
    private lateinit var noItem: TextView
    private lateinit var cartList: RecyclerView
    private lateinit var totalText: TextView
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private lateinit var checkoutBtn: Button
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var accountBtn: Button
    private lateinit var backBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_cart)
        // Create a gradient drawable
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ContextCompat.getColor(this, R.color.creamy_yellow),
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.red)
            )
        )

        // Set the gradient drawable as the background
        window.setBackgroundDrawable(gradientDrawable)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedViewModel.loadCartItems()
        cartList = findViewById(R.id.cartView)
        totalText = findViewById(R.id.total)
        noItem = findViewById(R.id.noItem)
        noItem.isVisible = false
        checkoutBtn = findViewById(R.id.checkoutBtn)
        accountBtn = findViewById(R.id.cartAccountBtn)
        backBtn = findViewById(R.id.viewBackBtn)
        totalText.text = "Total: $${intent.getStringExtra("cartTotal")}.00"
        totalText.text = sharedViewModel.totalPrice.toString()
        sharedViewModel.loadCartItems()
        sharedViewModel.cartItems.observe(this) { items ->
            if (items.isEmpty()) {
                noItem.isVisible = true
            } else {
                // Update the RecyclerView adapter with the items
                val adapter = CartAdapter(items, this, Product())
                cartList.adapter = adapter
                cartList.layoutManager = LinearLayoutManager(this)
                checkoutBtn.isEnabled = true
            }
        }
        updateTotal()

        backBtn.setOnClickListener{
            val intent = Intent(this@ViewCart, Product::class.java)
            startActivity(intent)
        }
        accountBtn.setOnClickListener{
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }
        checkoutBtn.setOnClickListener {
            val cartTotal = sharedViewModel.totalPrice
            val intent = Intent(this@ViewCart, Checkout::class.java)
            intent.putExtra("CcartTotal", cartTotal.toString())
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        sharedViewModel.loadCartItems()
    }

    fun updateTotal(){
        sharedViewModel.loadCartItems()
        sharedViewModel.totalPrice.observe(this) { totalPrice ->
            // Update the UI with the new total price
            totalText.text = "Total: $$totalPrice"
        }
    }
}