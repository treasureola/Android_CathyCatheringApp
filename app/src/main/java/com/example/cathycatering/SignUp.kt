package com.example.cathycatering

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {
    private lateinit var nameText: TextView
    private lateinit var namePhone: TextView
    private lateinit var nameEmail: TextView
    private lateinit var namePassword: TextView
    private lateinit var signUpButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var confirmText: TextView
    private lateinit var logInBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nameText = findViewById(R.id.signUpName)
        namePhone = findViewById(R.id.signUpPhone)
        nameEmail = findViewById(R.id.signUpEmail)
        namePassword = findViewById(R.id.signUpPassword)
        signUpButton = findViewById(R.id.SignUpBtn)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        confirmText = findViewById(R.id.confirm)
        logInBtn = findViewById(R.id.signUpLogBtn)

        val name = nameText.text.toString()
        val reference = firebaseDatabase.getReference("UserInfo/$name")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val infos = mutableListOf<UserInfo>()
                snapshot.children.forEach{ childSnapshot: DataSnapshot ->
                    val info: UserInfo?=childSnapshot.getValue(UserInfo::class.java)
                    if (info!=null){
                        infos.add(info)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CATHYFIREBASE", "FAILED TO CONNECT")
            }
        })

        logInBtn.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity((intent))
        }
        nameText.addTextChangedListener(textWatcher)
        namePassword.addTextChangedListener(textWatcher)
        namePhone.addTextChangedListener(textWatcher)
        nameEmail.addTextChangedListener(textWatcher)
        confirmText.addTextChangedListener(textWatcher)

        signUpButton.setOnClickListener{
            if (namePassword.text.toString() == confirmText.text.toString()){
                Log.e("CATHYFIREBASE", "EQUALS PASSWORD $namePassword")
                Log.e("CATHYFIREBASE", "EQUALS CONFIRM PASSOWRD $confirmText")
                val inputtedEmail: String  = nameEmail.text.toString().trim()
                val inputtedPassword: String  = namePassword.text.toString().trim()

                firebaseAuth.createUserWithEmailAndPassword(inputtedEmail, inputtedPassword)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            val user = firebaseAuth.currentUser
                            Toast.makeText(this,"created user: ${user!!.email}", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SignUp,Login::class.java)
                            startActivity(intent)
                        }else{
                            val exception = it.exception
                            AlertDialog.Builder(this)
                                .setTitle("oops")
                                .setMessage("$exception")
                                .show()
                        }
                    }
                val userName = nameText.text.toString()
                val userEmail = nameEmail.text.toString()
                val userPhone = namePhone.text.toString()
                val info = UserInfo(
                    userName = userName,
                    userEmail = userEmail,
                    userPhone = userPhone
                )
                reference.push().setValue(info)
            }else{
                Toast.makeText(this, "PASSWORDS DOESN'T MATCH", Toast.LENGTH_LONG).show()
            }
        }
    }
    private val textWatcher: TextWatcher =object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername:String=nameText.text.toString()
            val inputtedPassword:String=namePassword.text.toString()
            val inputtedEmail:String=nameEmail.text.toString()
            val inputtedPhone:String=namePhone.text.toString()
            val inputtedConfirm: String = confirmText.text.toString()


            val enableButton: Boolean=inputtedUsername.isNotBlank()&& inputtedPassword.isNotBlank()&&inputtedEmail.isNotBlank()&&inputtedPhone.isNotBlank()&&inputtedConfirm.isNotBlank()
            signUpButton.setEnabled(enableButton)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}