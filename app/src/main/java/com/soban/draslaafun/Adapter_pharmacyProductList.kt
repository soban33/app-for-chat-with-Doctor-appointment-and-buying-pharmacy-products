package com.soban.draslaafun

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class Adapter_pharmacyProductList(var productList: ArrayList<Model_pharmacyProductList>): RecyclerView.Adapter<Adapter_pharmacyProductList.ProductListViewHolder>() {

    class ProductListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val img_productImage = itemView.findViewById<ImageView>(R.id.img_productImage)
        val txt_productName = itemView.findViewById<TextView>(R.id.txt_productName)
        val txt_productUsage = itemView.findViewById<TextView>(R.id.txt_productUsage)
        val txt_productPrice = itemView.findViewById<TextView>(R.id.txt_productPrice)
        val txt_productCuttedPrice = itemView.findViewById<TextView>(R.id.txt_productCuttedPrice)
        val txt_productPriceDiscountPercent = itemView.findViewById<TextView>(R.id.txt_productPriceDiscountPercent)
        val txt_productIndication = itemView.findViewById<TextView>(R.id.txt_productIndication)
        val txt_directionOfUse = itemView.findViewById<TextView>(R.id.txt_directionOfUse)
        val txt_productFormula = itemView.findViewById<TextView>(R.id.txt_productFormula)
        val txt_addToCart = itemView.findViewById<TextView>(R.id.txt_addToCart)
        val txt_deleteFromCart = itemView.findViewById<TextView>(R.id.txt_deleteFromCart)
        val ll_mainDetailOfProduct_visible = itemView.findViewById<LinearLayout>(R.id.ll_mainDetailOfProduct_visible)
        val ll_additionalDetailsOfProduct_invisible = itemView.findViewById<LinearLayout>(R.id.ll_additionalDetailsOfProduct_invisible)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.singleitem_pharmacyprodlist,parent,false)
        return ProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {

        val dataOfProductFromFirebase: Model_pharmacyProductList = productList[position]

        Picasso.get().load(dataOfProductFromFirebase.productImage).into(holder.img_productImage)
        holder.img_productImage.clipToOutline = true
        holder.txt_productName.text = dataOfProductFromFirebase.productName
        holder.txt_productUsage.text = dataOfProductFromFirebase.productUsage
        holder.txt_productPrice.text = dataOfProductFromFirebase.productPrice
        holder.txt_productCuttedPrice.text = dataOfProductFromFirebase.productCuttedPrice
        holder.txt_productPriceDiscountPercent.text = dataOfProductFromFirebase.productDiscount
        holder.txt_productIndication.text = dataOfProductFromFirebase.productIndicatio
        holder.txt_directionOfUse.text = dataOfProductFromFirebase.directionofUse
        holder.txt_productFormula.text = dataOfProductFromFirebase.productFormula

        //getting uid of currently logged in user
        val uidOfLoggedinUser = FirebaseAuth.getInstance().currentUser

        //making the cutted text
        holder.txt_productCuttedPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        //setting clicklistener on add to cart button
        holder.txt_addToCart.setOnClickListener { val v: View = View(it.context)

            if(uidOfLoggedinUser == null){
                v.context.startActivity(Intent(v.context,Activity_Login::class.java))
            }else{
                val product = Model_pharmacyProductList(dataOfProductFromFirebase.productImage,
                    dataOfProductFromFirebase.productName,
                    dataOfProductFromFirebase.productUsage,
                    dataOfProductFromFirebase.productPrice,
                    dataOfProductFromFirebase.productCuttedPrice,
                    dataOfProductFromFirebase.productDiscount,
                    dataOfProductFromFirebase.productIndicatio,
                    dataOfProductFromFirebase.directionofUse,
                    dataOfProductFromFirebase.productFormula,
                    type = "cart"
                )

                //putting data to firebase
                FirebaseDatabase.getInstance().getReference("/cart").child(uidOfLoggedinUser.uid).push().setValue(product)
                    .addOnSuccessListener {
                        Toast.makeText(v.context,"item added to cart",Toast.LENGTH_SHORT).show()
                    }
            }
        }

        if (dataOfProductFromFirebase.type.equals("cart")){
            holder.txt_deleteFromCart.visibility = View.VISIBLE
        }else{
            holder.txt_addToCart.visibility = View.VISIBLE
        }

        //setting clicklistener to delete button
        holder.txt_deleteFromCart.setOnClickListener {
            /*FirebaseDatabase.getInstance().getReference("/cart").child(uidOfLoggedinUser).removeValue()
                .addOnSuccessListener {
                    Log.d("delete","success")
                }
                .addOnFailureListener {
                    Log.d("delete","failed")
                }*/
            val v: View = View(it.context)
            //Toast.makeText(v.context,,Toast.LENGTH_SHORT).show()
        }

        holder.ll_mainDetailOfProduct_visible.setOnClickListener {
            val isExpanded: Boolean = productList[position].isExpanded
            val ver = productList[position]

            if (isExpanded){
                holder.ll_additionalDetailsOfProduct_invisible.visibility = View.VISIBLE
                ver.isExpanded = !ver.isExpanded
            }
            else {
                holder.ll_additionalDetailsOfProduct_invisible.visibility = View.GONE
                ver.isExpanded = !ver.isExpanded
            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

}