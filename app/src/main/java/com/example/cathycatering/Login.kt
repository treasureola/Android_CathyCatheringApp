package com.example.cathycatering

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var userEmail: TextView
    private lateinit var userPassword: TextView
    private lateinit var logInBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUp: Button
    private lateinit var checkBox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userEmail = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        logInBtn = findViewById(R.id.logInBtn)
        firebaseAuth = FirebaseAuth.getInstance()
        signUp = findViewById(R.id.logInSignBtn)
        checkBox = findViewById(R.id.checkBox)
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val isChecked = sharedPreferences.getBoolean("checkbox_key", false)
        checkBox.isChecked = isChecked

        userEmail.addTextChangedListener(textWatcher)
        userPassword.addTextChangedListener(textWatcher)

        if (checkBox.isChecked){
            val inputEmail = sharedPreferences.getString("Email"," ")
            userEmail.text = inputEmail
            val inputPassword = sharedPreferences.getString("Password","")
            userPassword.text = inputPassword
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("checkbox_key", isChecked)
            editor.putString("Email",userEmail.text.toString())
            editor.putString("Password",userPassword.text.toString())
            editor.apply()
        }

        signUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        logInBtn.setOnClickListener{
            val inputtedUserName: String  = userEmail.text.toString().trim()
            val inputtedPassword: String  = userPassword.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(inputtedUserName, inputtedPassword)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = firebaseAuth.currentUser
                        val intent = Intent(this@Login,Product::class.java)
                        startActivity(intent)
                    }else{
                        val exception = it.exception
                        AlertDialog.Builder(this)
                            .setTitle("oops")
                            .setMessage("$exception")
                            .show()
                    }
                }
        }
    }
    private val textWatcher: TextWatcher =object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername:String=userEmail.text.toString()
            val inputtedPassword:String=userPassword.text.toString()

            val enableButton: Boolean=inputtedUsername.isNotBlank()&& inputtedPassword.isNotBlank()
            logInBtn.setEnabled(enableButton)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}