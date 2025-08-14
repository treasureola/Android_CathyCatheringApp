package com.example.cathycatering

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

class Checkout : AppCompatActivity() {
    private lateinit var total: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeOrderBtn: Button
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private lateinit var backToCartBtn: Button
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var accountBtn: Button
    private lateinit var indicate: TextView
    private lateinit var cardNo: TextView
    private lateinit var cvv: TextView
    private lateinit var exp: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        total = findViewById(R.id.checkoutPgTotal)
        recyclerView = findViewById(R.id.checkoutPgRecyceView)
        placeOrderBtn = findViewById(R.id.submitBtn)
        backToCartBtn = findViewById(R.id.backToCartBtn)
        accountBtn = findViewById(R.id.checkoutAccountBtn)
        indicate = findViewById(R.id.indicateText)
        cardNo = findViewById(R.id.cardNoText)
        cvv = findViewById(R.id.cvvText)
        exp = findViewById(R.id.expText)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        total.text = "Total: $${intent.getStringExtra("CcartTotal")}.00"
        accountBtn.setOnClickListener{
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }
        val email = firebaseAuth.currentUser?.email
        val name = email!!.substringBefore("@") + "Orders"
        val reference = firebaseDatabase.getReference("$name")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val infos = mutableListOf<OrderInfo>()
                snapshot.children.forEach{ childSnapshot: DataSnapshot ->
                    val info: OrderInfo?=childSnapshot.getValue(OrderInfo::class.java)
                    if (info!=null){
                        infos.add(info)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CATHYFIREBASE", "FAILED TO CONNECT")
            }
        })

        var t  = mutableListOf<CartItem>()
        sharedViewModel.loadCartItems()
        sharedViewModel.cartItems.observe(this) { items ->
            t = items
            val adapter = CheckoutAdapter(items, this, Product())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        updateTotal()

        backToCartBtn.setOnClickListener{
            val intent = Intent(this, ViewCart::class.java)
            startActivity(intent)
        }
        indicate.addTextChangedListener(textWatcher)
        cardNo.addTextChangedListener(textWatcher)
        cvv.addTextChangedListener(textWatcher)
        exp.addTextChangedListener(textWatcher)

        placeOrderBtn.setOnClickListener{
            val emails = firebaseAuth.currentUser?.email
            val sdf = SimpleDateFormat("'Order Placed On ' MM-dd-yyyy HH:mm:ss z")
            val currentDateAndTime = sdf.format(Date())
            val info = OrderInfo(
                userEmail = emails.toString(),
                orderDate = currentDateAndTime,
                product = t
            )
            reference.push().setValue(info)
            val intent = Intent(this,Confirm::class.java)
            startActivity(intent)
//            clear cart from database
            Log.e("CATHYFIREBASE", "FAILED TO CONNECT $t")
            for (i in t){
                sharedViewModel.cartItems.observe(this) { items ->
                    sharedViewModel.removeItemFromCart(i.product.productName)
                }
            }
        }
    }
    fun updateTotal(){
        sharedViewModel.loadCartItems()
        sharedViewModel.totalPrice.observe(this) { totalPrice ->
            // Update the UI with the new total price
            total.text = "Total: $$totalPrice"
        }
    }
    private val textWatcher: TextWatcher =object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername:String=indicate.text.toString()
            val inputtedPassword:String=cardNo.text.toString()
            val inputtedEmail:String=cvv.text.toString()
            val inputtedPhone:String=exp.text.toString()

            val enableButton: Boolean=inputtedUsername.isNotBlank()&& inputtedPassword.isNotBlank()&&inputtedEmail.isNotBlank()&&inputtedPhone.isNotBlank()
            placeOrderBtn.setEnabled(enableButton)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}