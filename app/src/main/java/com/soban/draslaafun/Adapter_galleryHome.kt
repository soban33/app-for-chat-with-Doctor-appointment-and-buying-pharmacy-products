package com.soban.draslaafun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_galleryHome(var imageList: ArrayList<Model_galleyHome>): RecyclerView.Adapter<Adapter_galleryHome.ImageViewHolder>() {

    class ImageViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        fun bind(modelGalleyhome: Model_galleyHome){
            val img_gallerysingleimage = itemView.findViewById<ImageView>(R.id.img_gallerysingleimage)

            Picasso.get().load(modelGalleyhome.imageLink).into(img_gallerysingleimage)
            img_gallerysingleimage.clipToOutline = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.singleitem_gallerymainscreen,parent,false)
        return Adapter_galleryHome.ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}