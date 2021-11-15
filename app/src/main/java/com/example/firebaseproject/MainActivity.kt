package com.example.firebaseproject

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var tvText : TextView
    private lateinit var signbtn : Button
    private lateinit var createbtn : Button
    private lateinit var email_txt : TextView
    private lateinit var pass_txt : TextView


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
        tvText = findViewById(R.id.tvText)
        signbtn = findViewById(R.id.sign_btn)
        createbtn = findViewById(R.id.create_btn)
        email_txt = findViewById(R.id.email_txt)
        pass_txt = findViewById(R.id.pass_txt)

        signbtn.setOnClickListener{
            var email = email_txt.text.toString()
            var pass = pass_txt.text.toString()

            if(!isEmpty(email) && !isEmpty(pass)) {
                //signIn( "test.maree.v@gmail.com", "123456")
                signbtn.isEnabled = false
                signIn(email, pass)
            }else {
                Toast.makeText(applicationContext, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }

        createbtn.setOnClickListener{
            var email = email_txt.text.toString()
            var pass = pass_txt.text.toString()

            if(!isEmpty(email) && !isEmpty(pass)) {
                createbtn.isEnabled = false
                createAccount(email, pass)
            }else {
                Toast.makeText(applicationContext, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }


    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user,"create")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null, "")
                }
                createbtn.isEnabled = true
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user,"login")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null,"")
                }
                signbtn.isEnabled = true
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?, type : String?) {
        var type_string = ""
        when(type){
            "login" -> type_string = "logged"
            "create" -> type_string = "created"
        }

        if( user != null ) {
            tvText.text = "Email usuari: ${user.email} $type_string"
        }else{
            if(type == "create"){
                tvText.text = "Aquest usuari ya existeix."
            }else{
                tvText.text = "No s'ha identificat"
            }

        }
    }


    private fun reload() {
        val currentUser = auth.currentUser
        updateUI(currentUser,"login")
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}