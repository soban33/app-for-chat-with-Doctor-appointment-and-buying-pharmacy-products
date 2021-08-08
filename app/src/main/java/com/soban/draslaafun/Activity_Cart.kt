package com.soban.draslaafun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Activity_Cart : AppCompatActivity() {

    lateinit var recyclerViewCartitemsList: RecyclerView
    lateinit var cartItemsList: ArrayList<Model_pharmacyProductList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //typecasting views
        val img_backbtncart = findViewById<ImageView>(R.id.img_backbtncart)

        recyclerViewCartitemsList = findViewById(R.id.recyclerview_cartitemslist)
        recyclerViewCartitemsList.setHasFixedSize(true)

        cartItemsList = arrayListOf<Model_pharmacyProductList>()

        val uidOfLoggedinUser = FirebaseAuth.getInstance().currentUser?.uid!!

        val ref = FirebaseDatabase.getInstance().getReference("/cart").child(uidOfLoggedinUser)
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val data: Model_pharmacyProductList = it.getValue(Model_pharmacyProductList::class.java)!!
                    Log.d("itemid","${it.key}")
                    cartItemsList.add(data)
                }
                recyclerViewCartitemsList.adapter = Adapter_pharmacyProductList(cartItemsList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        img_backbtncart.setOnClickListener {
            finish()
        }

    }
}