package com.example.firebaseproject

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RtDatabase : AppCompatActivity() {

    private lateinit var modificaBtn : Button
    private lateinit var textName : TextView
    private lateinit var textInfo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rt_database)

        modificaBtn = findViewById(R.id.modifica_btn)
        textName = findViewById(R.id.text_name)
        textInfo = findViewById(R.id.text_info)

        // Write a message to the database
        val database = FirebaseDatabase.getInstance("https://fir-project-2f1ed-default-rtdb.europe-west1.firebasedatabase.app/")
        //val database = Firebase.database
        val myRef = database.getReference("message")


        modificaBtn.setOnClickListener {
            myRef.setValue(textName.text.toString())
            modificaBtn.isEnabled = false
        }

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<String>()
                textInfo.text = value
                modificaBtn.isEnabled = true
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                textInfo.text = "error de lectura"
                modificaBtn.isEnabled = true

            }



        })



    }
}
