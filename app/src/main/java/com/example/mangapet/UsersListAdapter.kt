package com.example.mangapet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UsersListAdapter(var user: List<User> = emptyList()): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val listItemView = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return RecyclerViewHolder(listItemView)
    }

    override fun getItemCount(): Int {
        return user.count()
    }


    fun updateUserList(itemsToUpdate: List<User>){
        user = itemsToUpdate
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.name.text = user[position].displayName
        holder.email.text = user[position].email
        val imageUrl = user[position].pfImage
        Picasso.get()
            .load(imageUrl)
            .into(holder.image)
    }
}

class RecyclerViewHolder (item: View): RecyclerView.ViewHolder(item){
    val email = item.findViewById(R.id.email) as TextView
    val name: TextView = item.findViewById(R.id.name)
    val image = item.findViewById<ImageView>(R.id.pfImage)

}