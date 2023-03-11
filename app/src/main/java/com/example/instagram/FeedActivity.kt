package com.example.instagram

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.instagram.adapter.FeedRecyclerAdapter
import com.example.instagram.databinding.ActivityFeedBinding
import com.example.instagram.model.Post
import com.google.common.base.MoreObjects.ToStringHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFeedBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var postArrayList:ArrayList<Post>
    private lateinit var feedAdapter:FeedRecyclerAdapter
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        db= Firebase.firestore
        postArrayList=ArrayList<Post>()
        getData()
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        feedAdapter= FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter=feedAdapter


        /**
         * Deneme
         */

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.add_post){
            val intent=Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId==R.id.sign_out)
        {
            auth.signOut()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null)
            {
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else
            {
                if (value!=null){
                    if (!value.isEmpty){
                       val documents=value.documents
                        postArrayList.clear()  //-> Tarihe göre düzgün sıralasın diye
                        for (document in documents){
                            //casting
                            val comment=document.get("comment") as String
                            val useremail=document.get("useremail") as String
                            val downloadUrl=document.get("downloadURL") as String
                            val post=Post(useremail,comment,downloadUrl)
                            postArrayList.add(post)
                            println(comment)
                        }
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}