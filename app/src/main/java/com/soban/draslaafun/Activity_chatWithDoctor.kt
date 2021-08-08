package com.soban.draslaafun

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class Activity_chatWithDoctor : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var chatList: ArrayList<Model_chat>

    lateinit var progressDialog: ProgressDialog

    lateinit var uidOfLoggedinUser: String

    var linearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_doctor)

        //type casting all views
        val btn_sendMessage = findViewById<Button>(R.id.btn_sendMessage)
        val edttxt_typeMessageField = findViewById<EditText>(R.id.edttxt_typeMessageField)
        val img_sendimageinchat = findViewById<ImageView>(R.id.img_cartpharmacy)
        val img_sendaudioinchat = findViewById<ImageView>(R.id.img_sortpharmacy)

        //getting uid of currently logged in user
        uidOfLoggedinUser = FirebaseAuth.getInstance().currentUser?.uid!!

        //configure progress dialogue
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Uploading image...")
        progressDialog.setCanceledOnTouchOutside(false)



        recyclerView = findViewById(R.id.RecyclerView_chats)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        chatList = arrayListOf()

        //listening for messages and retrieving messages
        val ref = FirebaseDatabase.getInstance().getReference("/chats").child(uidOfLoggedinUser)
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatData: Model_chat = snapshot.getValue(Model_chat::class.java)!!
                Log.d("chat",chatData.message)

                chatList.add(chatData)
                recyclerView.adapter = Adapter_chatuser(chatList)

                linearLayoutManager.scrollToPosition(recyclerView.adapter!!.itemCount -1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


        //sending text messages
        btn_sendMessage.setOnClickListener {
            val message = edttxt_typeMessageField.text.toString()
            val chatData = Model_chat(message,uidOfLoggedinUser,"text","")

            if (message.isEmpty()){

            }else{
                FirebaseDatabase.getInstance().getReference("/chats").child(uidOfLoggedinUser).push().setValue(chatData)
                    .addOnSuccessListener {
                        edttxt_typeMessageField.text.clear()
                        //recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
                    }
            }
        }

        img_sendimageinchat.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,25)
        }

    }

    //fetching the image from local storage and sending in chat message
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 25){
            if (data != null){
                val uri = data.data
                val ref = FirebaseStorage.getInstance().getReference().child("/chatimages").child(Calendar.getInstance().timeInMillis.toString())
                progressDialog.show()
                ref.putFile(uri!!)
                    .addOnSuccessListener {

                        ref.downloadUrl.addOnSuccessListener {
                            progressDialog.dismiss()
                            val imageUrl = it.toString()

                            val chatData = Model_chat("",uidOfLoggedinUser,"image",imageUrl)
                            FirebaseDatabase.getInstance().getReference("/chats").child(uidOfLoggedinUser).push().setValue(chatData)
                                .addOnSuccessListener {
                                    //recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener{
                        Toast.makeText(this,"image upload failed",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

}