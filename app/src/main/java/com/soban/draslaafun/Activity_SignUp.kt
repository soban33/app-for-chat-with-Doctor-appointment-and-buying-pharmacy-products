package com.soban.draslaafun

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Activity_SignUp : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        //configure progress dialogue
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)


        //type casting all the views
        val edttxt_userName = findViewById<EditText>(R.id.edttxt_userName)
        val edttxt_userEmail = findViewById<EditText>(R.id.edttxt_userEmail)
        val edttxt_userPassword = findViewById<EditText>(R.id.edttxt_userPassword)
        val btn_register = findViewById<Button>(R.id.btn_register)
        val txt_login = findViewById<TextView>(R.id.txt_login)
        val txt_autoComplete_gender = findViewById<AutoCompleteTextView>(R.id.txt_autoComplete_gender)
        val edttxt_userAge = findViewById<EditText>(R.id.edttxt_userAge)

        //setting the code for selecting gender in signup form
        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this,R.layout.singleitem_dropdown_gender,gender)
        txt_autoComplete_gender.setAdapter(arrayAdapter)


        txt_login.setOnClickListener {
            val intent = Intent(this,Activity_Login::class.java)
            startActivity(intent)
            finish()
        }

        btn_register.setOnClickListener {
            if (TextUtils.isEmpty(edttxt_userName.text.toString())){
                edttxt_userName.setError("Please enter name")
            }else if (TextUtils.isEmpty(edttxt_userAge.text.toString())){
                edttxt_userAge.setError("Please enter age")
            }else if (TextUtils.isEmpty(txt_autoComplete_gender.text.toString())){
                txt_autoComplete_gender.setError("Please select gender")
            }else if (!Patterns.EMAIL_ADDRESS.matcher(edttxt_userEmail.text.toString()).matches()){
               edttxt_userEmail.setError("Please enter valid email")
            }else if (TextUtils.isEmpty(edttxt_userPassword.text.toString())){
               edttxt_userPassword.setError("Please enter password")
            }else if (edttxt_userPassword.text.toString().length < 6){
               edttxt_userPassword.setError("Password must contain 6 characters")
            }else{
               progressDialog.show()
               auth.createUserWithEmailAndPassword(edttxt_userEmail.text.toString(), edttxt_userPassword.text.toString())
                   .addOnSuccessListener {

                       //now create user in realtime database
                        //making hashmap for user data
                       val userDataMap = HashMap<String, Any>()
                       userDataMap["name"] = edttxt_userName.text.toString()
                       userDataMap["email"] = edttxt_userEmail.text.toString()
                       userDataMap["gender"] = txt_autoComplete_gender.text.toString()
                       userDataMap["age"] = edttxt_userAge.text.toString()

                       val ref = FirebaseDatabase.getInstance().getReference("users")
                           .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                           .setValue(userDataMap)
                           .addOnSuccessListener {
                               progressDialog.dismiss()
                               Toast.makeText(this,"Account created succesfully", Toast.LENGTH_LONG).show()

                               //starting new activity
                               startActivity(Intent(this,Activity_chatWithDoctor::class.java))
                               finish()
                           }
                           .addOnFailureListener {
                               progressDialog.dismiss()
                               Toast.makeText(this,"signup error",Toast.LENGTH_SHORT).show()
                           }
                   }
                   .addOnFailureListener {
                       progressDialog.dismiss()
                       Toast.makeText(this,"signup error",Toast.LENGTH_SHORT).show()
                   }
           }
        }
    }
}