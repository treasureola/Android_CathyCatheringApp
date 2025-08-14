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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistory : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var nameText: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        backBtn = findViewById(R.id.backBtn)
        nameText = findViewById(R.id.nameText)
        bBtn = findViewById(R.id.bBtn)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        backBtn.setOnClickListener{
            val intent = Intent(this, Product::class.java)
            startActivity(intent)
        }

        bBtn.setOnClickListener{
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }

        val goods = mutableListOf<CartItem>()
        val email = firebaseAuth.currentUser?.email
        val time = intent.getStringExtra("Date").toString()
        val path = email!!.substringBefore("@") + "Orders"
        val reference = firebaseDatabase.getReference(path)
        val query = reference.orderByChild("orderDate").equalTo(time)
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    Log.e("CATHYFIREBASE", "------- $dataSnapshot1")
                    val user = dataSnapshot1.getValue(OrderInfo::class.java)
                    if (user != null) {
                        for (i in user.product){
                            goods.add(i)
                        }
                    }
                }
                val adapter = OrderAdapter(goods)
                historyRecyclerView.adapter = adapter
                historyRecyclerView.layoutManager = LinearLayoutManager(this@OrderHistory)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CATHYFIREBASE", "FAILED TO CONNECT")
            }
        })
    }
}