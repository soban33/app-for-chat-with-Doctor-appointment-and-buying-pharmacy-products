package com.soban.draslaafun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_serviceshome(var serviceList: ArrayList<Model_serviceshome>, val clickListener: (Model_serviceshome) -> Unit): RecyclerView.Adapter<Adapter_serviceshome.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(modelServiceshome: Model_serviceshome,clickListener: (Model_serviceshome) -> Unit){
            val img_serviceimage = itemView.findViewById(R.id.img_serviceimage) as ImageView
            val txt_servicename = itemView.findViewById(R.id.txt_servicename) as TextView
            val txt_serviceshortdesc = itemView.findViewById(R.id.txt_serviceshortdesc) as TextView

            //Picasso.get().load(modelServiceshome.service_imagr).into(img_serviceimage)
            txt_servicename.text = modelServiceshome.service_name
            txt_serviceshortdesc.text = modelServiceshome.short_description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.singleitem_servicesmainscreen,parent,false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(serviceList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}