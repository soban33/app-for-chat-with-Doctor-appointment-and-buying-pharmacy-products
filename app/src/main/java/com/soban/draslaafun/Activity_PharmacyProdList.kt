package com.soban.draslaafun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Activity_PharmacyProdList : AppCompatActivity() {

    lateinit var recyclerViewPharmacyProdList: RecyclerView
    lateinit var pharmacyProdList: ArrayList<Model_pharmacyProductList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_prod_list)

        recyclerViewPharmacyProdList = findViewById(R.id.recyclerview_pharmacyProductList)
        recyclerViewPharmacyProdList.setHasFixedSize(true)

        pharmacyProdList = arrayListOf<Model_pharmacyProductList>()

        //fetching data from realtime database for our services
        val refServices = FirebaseDatabase.getInstance().getReference("/PharmacyProductList")
        refServices.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val data: Model_pharmacyProductList = it.getValue(Model_pharmacyProductList::class.java)!!
                    Log.d("productdata","$data")

                    pharmacyProdList.add(data)

                }

                recyclerViewPharmacyProdList.adapter = Adapter_pharmacyProductList(pharmacyProdList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


}