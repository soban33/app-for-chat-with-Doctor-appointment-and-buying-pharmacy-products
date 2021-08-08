package com.soban.draslaafun

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.lang.Exception


class MainActivity : AppCompatActivity(), (Model_serviceshome) -> Unit {

    //global variables for recyclerview of services
    lateinit var recyclerViewOurServices: RecyclerView
    lateinit var serviceList : ArrayList<Model_serviceshome>

    //global variables for recyclerview of gallery
    lateinit var recyclerview_GalleryHome : RecyclerView
    lateinit var imageList : ArrayList<Model_galleyHome>

    //global variables for location
    var latitude = ""
    var longitude = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //binding variables with views
        recyclerViewOurServices = findViewById(R.id.recyclerview_OurServices)
        recyclerViewOurServices.setHasFixedSize(true)

        recyclerview_GalleryHome = findViewById(R.id.recyclerview_GalleryHome)
        recyclerview_GalleryHome.setHasFixedSize(true)

        serviceList = arrayListOf<Model_serviceshome>()
        imageList = arrayListOf<Model_galleyHome>()

        //variable for firebase auth instance
        val currentUser = FirebaseAuth.getInstance().currentUser


        //binding views with variables for setting clicklistener
        //texts
        val txt_locationHome = findViewById<TextView>(R.id.txt_locationHome)
        val txt_callHome = findViewById<TextView>(R.id.txt_callHome)
        val txt_timingHome = findViewById<TextView>(R.id.txt_timingHome)
        //images
        val img_carticon = findViewById<ImageView>(R.id.img_carticon)
        val img_burgermenuicon = findViewById<ImageView>(R.id.img_burgermenuicon)
        val img_whatsAppHome = findViewById<ImageView>(R.id.img_whatsAppHome)
        val img_callHome = findViewById<ImageView>(R.id.img_callHome)
        val img_gmailHome = findViewById<ImageView>(R.id.img_gmailHome)
        val img_instaHome = findViewById<ImageView>(R.id.img_instaHome)
        //cards
        val card_aboutDoctorHome = findViewById<LinearLayout>(R.id.card_aboutDoctorHome)
        val card_ourPharmacyHome = findViewById<LinearLayout>(R.id.card_ourPharmacyHome)
        val card_chatWithDoctorHome = findViewById<LinearLayout>(R.id.card_chatWithDoctorHome)

        img_carticon.setOnClickListener {
            if (currentUser != null){
                startActivity(Intent(this,Activity_Cart::class.java))
            }else{
                startActivity(Intent(this, Activity_Login::class.java))
            }
        }

        //fetching data from realtime database for our services
        val refServices = FirebaseDatabase.getInstance().getReference("/HomeScreen").child("/services")
        refServices.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val data: Model_serviceshome = it.getValue(Model_serviceshome::class.java)!!
                    Log.d("servicesdata","$data")

                    serviceList.add(data)

                }

                recyclerViewOurServices.adapter = Adapter_serviceshome(serviceList,this@MainActivity)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        //fetching data from realtime database for gallery
        val refGallery = FirebaseDatabase.getInstance().getReference("/HomeScreen").child("/gallery")
        refGallery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val data: Model_galleyHome = it.getValue(Model_galleyHome::class.java)!!
                    Log.d("imageLink","$data")

                    imageList.add(data)
                }

                recyclerview_GalleryHome.adapter = Adapter_galleryHome(imageList)
            }

            override fun onCancelled(error: DatabaseError) {  }

        })

        //getting lat and long from database and then intent to maps
        val refLocation = FirebaseDatabase.getInstance().getReference("/HomeScreen").child("/location")
        refLocation.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val locationData = p0.getValue(Model_locationHome::class.java)

                latitude = locationData!!.latitude
                longitude = locationData.longitude

                txt_locationHome.setOnClickListener {
                    val mTitle = "Dr Aslaaf Clinic"
                    val geoUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude ($mTitle)"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {  }

        })



        card_aboutDoctorHome.setOnClickListener {
            val intent = Intent(this, Activity_AboutDoctor::class.java)
            startActivity(intent)
        }
        card_ourPharmacyHome.setOnClickListener {
            val intent = Intent(this, Activity_PharmacyProdList::class.java)
            startActivity(intent)
        }
        card_chatWithDoctorHome.setOnClickListener {
            if (currentUser != null){
                val intent = Intent(this, Activity_chatWithDoctor::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, Activity_Login::class.java)
                startActivity(intent)
            }
        }



        txt_callHome.setOnClickListener {
            val mobileNum = "+919977108786"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(mobileNum)))
            startActivity(intent)
        }
        txt_timingHome.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_timinghome, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            mBuilder.show()
        }

        img_callHome.setOnClickListener{
            val mobileNum = "+919977108786"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(mobileNum)))
            startActivity(intent)
        }

        img_whatsAppHome.setOnClickListener {

            try{
                val intent = Intent()
                intent.action = Intent.ACTION_SENDTO
                intent.data = Uri.parse("smsto: +919977108786")
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            }catch (ex:Exception){
                Toast.makeText(this,"What's App not found!",Toast.LENGTH_SHORT).show()
            }
        }
        img_gmailHome.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SENDTO
            intent.data = Uri.parse("mailto: shaikhaslaaf@gmail.com")
            startActivity(intent)
        }
        img_instaHome.setOnClickListener {
            val uri = "https://www.instagram.com/aslaafshaikh/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }


    //function for setting clicklistener on our services
    override fun invoke(p1: Model_serviceshome) {

    }
}