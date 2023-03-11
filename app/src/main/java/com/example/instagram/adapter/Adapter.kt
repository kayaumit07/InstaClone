package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.databinding.RecyclerRowBinding
import com.example.instagram.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(val postList:ArrayList<Post>):RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>(){
    class PostHolder(val binding:RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
         val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text=postList.get(position).email.toString()
        holder.binding.recyclerCommentText.text=postList.get(position).comment.toString()
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.recyclerImageView)
        //holder.binding.recyclerImageView.setImageURI(postList.get(position).downloadUrl)
    }
}