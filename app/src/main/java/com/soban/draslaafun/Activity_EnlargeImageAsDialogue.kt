package com.soban.draslaafun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class Activity_EnlargeImageAsDialogue : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enlarge_image_as_dialogue)

        val imageurl = intent.getStringExtra("imgurl")

        val img_enlarged = findViewById<ImageView>(R.id.img_enlarged)
        img_enlarged.isClickable = true

        Picasso.get().load(imageurl).placeholder(R.mipmap.ic_imagewhite).into(img_enlarged)

        img_enlarged.setOnClickListener {
            finish()
        }
    }
}