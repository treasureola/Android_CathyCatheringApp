package com.example.cathycatering

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject

class ProductManager {
    val okHttpClient: OkHttpClient

    init{
        val builder= OkHttpClient.Builder()
        val loggingInterceptor=HttpLoggingInterceptor()
        loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient=builder.build()

    }
    suspend fun retrieveDessert(apikey:String): List<ProductInfo>
    {
        val request= Request.Builder()
            .url("https://pizza-and-desserts.p.rapidapi.com/desserts")
            .header("x-rapidapi-key","$apikey")
            .get()
            .build()
        val response: Response =okHttpClient.newCall(request).execute()
        val responseBody=response.body?.string()
        if (response.isSuccessful && !responseBody.isNullOrEmpty()){
            val yelps=mutableListOf<ProductInfo>()
            val json = JSONArray(responseBody)
            for (i in 0 until json.length()){
                val currentFood = json.getJSONObject(i)
                val name = currentFood.getString("name")
                val description = currentFood.getString("description")
                val image = currentFood.getString("img")
                val price = currentFood.getInt("price")
                val id = currentFood.getInt("id")
                val yelp = ProductInfo(productName = name,
                    productDescription = description,
                    productImage = image,
                    productPrice = price,
                    productId = id
                )
                yelps.add(yelp)
            }
            return yelps
        }else{
            return listOf()
        }

    }
}