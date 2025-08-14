package com.example.cathycatering

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.ArrayList

@Serializable
data class ProductInfo(
    val productName: String,
    val productDescription: String,
    val productPrice: Int,
    val productImage: String,
    val productId: Int
){
    constructor(): this("","",0,"",0)
}

@Serializable
data class CartItem(
    val id: Int,
    val product: ProductInfo,
    var quantity: Int
) : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }

    constructor(): this(0,ProductInfo("","",0,"",0), 0)
}

data class View(
    val items: CartItem,
){
    constructor(): this(CartItem())
}

@Serializable
object Cart{
    var items: MutableList<CartItem> = mutableListOf()
    var total: String = ""
    var quantity: Int = 0
}

@SuppressLint("StaticFieldLeak")
class SharedViewModel : ViewModel() {
    val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems
    val callback = ViewCart()

    private val _totalPrice = MutableLiveData<Int>(0)
    val totalPrice: LiveData<Int> = _totalPrice

    private val _totalCount = MutableLiveData<Int>(0)
    val totalCount: LiveData<Int> = _totalCount

    private val _sharedPrefs = MutableLiveData<SharedPreferences>()
    val sharedPrefs: LiveData<SharedPreferences> = _sharedPrefs

    private var firebaseAuth=FirebaseAuth.getInstance()
    val email = firebaseAuth.currentUser?.email
    val name = email!!.substringBefore("@") + "Cart"
    private val database = FirebaseDatabase.getInstance()

    fun init(context: Context) {
        val sharedPreferences = context.getSharedPreferences("my_cart_prefs", Context.MODE_PRIVATE)
        _sharedPrefs.value = sharedPreferences
    }

    fun addItemToCart(product: ProductInfo) {
        val newCartItem = CartItem(generateUniqueId(), product, 1)
        val currentCartItems = _cartItems.value ?: mutableListOf()

        currentCartItems.add(newCartItem)
        _cartItems.postValue(currentCartItems)
        _cartItems.value = currentCartItems
        if (Cart.items.any{it.product.productId == product.productId}){
            val index = Cart.items.indexOfFirst { it.product.productId == newCartItem.product.productId }
            newCartItem.quantity++
            Cart.items[index] = Cart.items[index].copy(quantity = newCartItem.quantity)
        }else{
            Cart.items.add(newCartItem)
        }
//        Cart.total = getTotalPrice().toString()
        saveCartItemsToFirebase() // New function
    }

    fun saveCartItemsToFirebase() {
        val cartItems = _cartItems.value ?: return
        val cartRef = database.getReference(name)

        for (item in cartItems) {
            val itemId = item.product.productName.toString()
            cartRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val existingItem = dataSnapshot.getValue(CartItem::class.java)
                    if (existingItem != null) {
                        val newQuantity = existingItem.quantity + 1
                        Log.e("FirebaseError", "Error updating item: ${newQuantity }")
                        updateItemQuantity(existingItem.product.productName, newQuantity)
//                        getTotalPrice()
                    } else {
                        item.quantity
                        cartRef.child(itemId).setValue(item)
//                        getTotalPrice()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                    Log.e("FirebaseError", "Error updating item: ${error.message}")
                }
            })
        }
    }

    private fun generateUniqueId(): Int {
        return (1..1000).random() // Replace with your desired logic
    }

    fun updateItemQuantity(productId: String, newQuantity: Int) {
        val cartRef = database.getReference(name)
        cartRef.child(productId).child("quantity").setValue(newQuantity)
    }

    fun loadCartItems() {
        viewModelScope.launch(Dispatchers.Main) {
            val cartRef = database.getReference(name)
            val snapshot = cartRef.get().await()
            val loadedItems = mutableListOf<CartItem>()
            for (child in snapshot.children) {
                val item = child.getValue(CartItem::class.java)
                if (item != null) {
                    loadedItems.add(item)
                }
            }
            _cartItems.postValue(loadedItems)
            val totalPrice = loadedItems.sumOf { it.product.productPrice * it.quantity }
            _totalPrice.postValue(totalPrice)

            val totalCount =loadedItems.size
            _totalCount.postValue(totalCount)
        }
    }
    fun removeItemFromCart(productId: String) {
        val cartRef = database.getReference(name)
        cartRef.child(productId).removeValue()
    }
}


interface ActivityCallback {
    fun updateTotal()
    fun updateCount()
}

