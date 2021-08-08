package com.soban.draslaafun

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import java.lang.Exception

class Activity_AboutDoctor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_doctor)

        val img_callAboutDoc = findViewById<ImageView>(R.id.img_callAboutDoc)
        val img_gmailAboutDoc = findViewById<ImageView>(R.id.img_gmailAboutDoc)
        val img_instaAboutDoc = findViewById<ImageView>(R.id.img_instaAboutDoc)
        val img_whatsAppAboutDoc = findViewById<ImageView>(R.id.img_whatsAppAboutDoc)

        img_callAboutDoc.setOnClickListener {
            val mobileNum = "+919977108786"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ Uri.encode(mobileNum)))
            startActivity(intent)
        }
        img_gmailAboutDoc.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SENDTO
            intent.data = Uri.parse("mailto: shaikhaslaaf@gmail.com")
            startActivity(intent)
        }
        img_instaAboutDoc.setOnClickListener {
            val uri = "https://www.instagram.com/aslaafshaikh/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
        img_whatsAppAboutDoc.setOnClickListener {
            try{
                val intent = Intent()
                intent.action = Intent.ACTION_SENDTO
                intent.data = Uri.parse("smsto: +919977108786")
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            }catch (ex: Exception){
                Toast.makeText(this,"What's App not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}