package com.example.instagram

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.instagram.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.ktx.options
import com.google.firebase.storage.FirebaseStorage
import org.checkerframework.checker.units.qual.Length

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        if (auth.currentUser!=null)
        {
            val intent=Intent(this@MainActivity,FeedActivity::class.java)
            startActivity(intent)
        }
        else
        {

        }




    }
    fun signinClicked(view:View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (email.equals("") || password.equals("")) {
                Toast.makeText(baseContext, "E mail or Password is empty", Toast.LENGTH_LONG).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener { task->

                    val intent=Intent(this@MainActivity,FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener(){
                    Toast.makeText(baseContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }


    }
    fun signupClicked(view:View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty())
        {
            if (email.equals("") || password.equals(""))
            {
                Toast.makeText(baseContext, "E mail or Password is empty", Toast.LENGTH_LONG).show()
            }
            else
            {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val intent=Intent(this@MainActivity,FeedActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Auth Failed", Toast.LENGTH_LONG).show()

                    }
                }
            }

        }
    }


}


