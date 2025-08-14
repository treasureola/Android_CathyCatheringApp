package com.example.cathycatering

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class Account : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var email: TextView
    private lateinit var orderList: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var accBackBtn: Button
    private lateinit var cartBackBtn: FloatingActionButton
    private lateinit var nullText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        name = findViewById(R.id.accountName)
        phone = findViewById(R.id.accountPhone)
        email = findViewById(R.id.accountEmail)
        orderList = findViewById(R.id.historyRecyclerView)
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        accBackBtn = findViewById(R.id.accBackBtn)
        cartBackBtn = findViewById(R.id.accCartBtn)
        nullText = findViewById(R.id.nullText)

        accBackBtn.setOnClickListener{
            val intent = Intent(this@Account, Product::class.java)
            startActivity((intent))
        }
        cartBackBtn.setOnClickListener{
            val intent = Intent(this@Account, ViewCart::class.java)
            startActivity(intent)
        }

        val currentEmail = firebaseAuth.currentUser!!.email
        var reference = firebaseDatabase.getReference("UserInfo")
        val queryEmail = reference.orderByChild("userEmail").equalTo(currentEmail)
        queryEmail.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    val user = dataSnapshot1.getValue(UserInfo::class.java)
                    if (user != null) {
                        val userEmail = user.userEmail
                        val userName = user.userName
                        val userPhone = user.userPhone
                        email.text = "EMAIL: $userEmail"
                        phone.text = "PHONE: $userPhone"
                        name.text = "NAME: $userName"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CATHYFIREBASE", "FAILED TO CONNECT")
            }
        })
        val dates = mutableListOf<OrderList>()
        val email = firebaseAuth.currentUser?.email
        val path = email!!.substringBefore("@") + "Orders"
        reference = firebaseDatabase.getReference(path)
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    val user: OrderInfo? = dataSnapshot1.getValue(OrderInfo::class.java)
                    if (user != null) {
                        val date = user.orderDate
                        dates.add(OrderList(date))
                    }
                }
                if (dates.isEmpty()){
                    nullText.isVisible = true
                }else{
                    val adapter = AccountAdapter(dates)
                    orderList.adapter = adapter
                    orderList.layoutManager = LinearLayoutManager(this@Account)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CATHYFIREBASE", "FAILED TO CONNECT")
            }
        })
    }
}