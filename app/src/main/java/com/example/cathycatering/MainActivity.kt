package com.example.cathycatering

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var toLogIn: Button
    private lateinit var toSignUp: Button
    private lateinit var spinner: Spinner
//    private var isLanguageInitialized = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        // Create a gradient drawable
//        val gradientDrawable = GradientDrawable(
//            GradientDrawable.Orientation.TOP_BOTTOM,
//            intArrayOf(
//                ContextCompat.getColor(this, R.color.Creamy_white),
//                ContextCompat.getColor(this, R.color.Cordovan),
//                ContextCompat.getColor(this, R.color.Golden_yellow)
//            )
//        )
//
//        // Set the gradient drawable as the background
//        window.setBackgroundDrawable(gradientDrawable)
        val sharedPreferences = getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", Locale.getDefault().language) ?: Locale.getDefault().language
//        setLocale(languageCode)
        val isLanguageInitialized = sharedPreferences.getBoolean("language_initialized", false)
        if (!isLanguageInitialized) {
            setLocale(languageCode)
            sharedPreferences.edit().putBoolean("language_initialized", true).apply()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        toLogIn = findViewById(R.id.toLogInBtn)
        toSignUp = findViewById(R.id.toSignUpBtn)
        spinner = findViewById(R.id.spinner)
//        val spinnerData = resources.getStringArray(R.array.countries)
        val languageOptions = arrayOf("English", "French", "German")
        val languageCodes = arrayOf("en", "fr", "de")
        val spinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,languageOptions)
        spinner.adapter = spinnerAdapter


//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val selectedLanguageCode = languageCodes[p2]
//                sharedPreferences.edit().putString("language", selectedLanguageCode).apply()
//                setLocale(selectedLanguageCode)
////                recreate()
////                val intent = intent
////                finish()
////                startActivity(intent)
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {}
//        }

        toLogIn.setOnClickListener{
            val intent = Intent(this@MainActivity,Login::class.java)
            startActivity(intent)
        }
        toSignUp.setOnClickListener{
            val intent = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intent)
        }
    }
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}