package com.soban.draslaafun

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class Adapter_chatuser(var chatList: ArrayList<Model_chat>): RecyclerView.Adapter<Adapter_chatuser.ChatUserViewHolder>() {

    private var TO_DOCTOR = 0
    private var FROM_DOCTOR = 1

    class ChatUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txt_message = itemView.findViewById<TextView>(R.id.txt_message)
        val img_chat = itemView.findViewById<ImageView>(R.id.img_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {

        if (viewType == TO_DOCTOR){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.singleitem_chatmsg_todoctor,parent,false)
            return ChatUserViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.singleitem_chatmsg_fromdoctor,parent,false)
            return ChatUserViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val chatData: Model_chat = chatList[position]

        if (chatData.type.equals("text")){
            holder.txt_message.visibility = View.VISIBLE
            holder.img_chat.visibility = View.GONE
            holder.txt_message.text = chatData.message
        }else if (chatData.type.equals("image")){
            holder.txt_message.visibility = View.GONE
            holder.img_chat.visibility = View.VISIBLE
            holder.img_chat.clipToOutline = true
            Picasso.get().load(chatData.image).placeholder(R.mipmap.ic_imagewhite).into(holder.img_chat)
        }

        holder.img_chat.setOnClickListener {
            val intent = Intent(it.context,Activity_EnlargeImageAsDialogue::class.java)
            intent.putExtra("imgurl",chatData.image)
            it.context.startActivity(intent)
        }

    }
   
    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatList[position].userId == FirebaseAuth.getInstance().currentUser?.uid){
            return TO_DOCTOR
        }else{
            return FROM_DOCTOR
        }
    }
}