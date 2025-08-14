package com.example.cathycatering

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Product : AppCompatActivity() {
    private lateinit var viewCartBtn: FloatingActionButton
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var quantityCount: TextView
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private lateinit var accountBtn: Button
    private lateinit var nullText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewCartBtn = findViewById(R.id.viewCartBtn)
        productRecyclerView = findViewById(R.id.productRecyclerView)
        quantityCount = findViewById(R.id.quantityCount)
        quantityCount.text = Cart.quantity.toString()
        accountBtn = findViewById(R.id.productAccountBtn)
        sharedViewModel.loadCartItems()

        sharedViewModel.totalCount.observe(this) { totalCount ->
            // Update the UI with the new total price
            quantityCount.text = "$totalCount"
        }

        accountBtn.setOnClickListener{
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }
        viewCartBtn.setOnClickListener{
//            val cartTotal = sharedViewModel.getTotalPrice()
            val intent = Intent(this@Product, ViewCart::class.java)
//            intent.putExtra("cartTotal", cartTotal.toString())
            startActivity(intent)
        }

        //Getting the data from API and loading it onto the recycler view
        val apikey = getString(R.string.foodAPI)
        var dessert = listOf<ProductInfo>()
        lifecycleScope.launch {
            withContext(IO) {
                dessert = ProductManager().retrieveDessert(apikey)
            }
            if (dessert.isEmpty()){
                nullText.isVisible = true
                Log.e("DATA LOAD", "ERROR")
            }else{
                val adapter = ProductAdapter(dessert,ViewCart(),this@Product)
                productRecyclerView.adapter = adapter
                productRecyclerView.layoutManager = LinearLayoutManager(this@Product)
            }
        }
    }
}