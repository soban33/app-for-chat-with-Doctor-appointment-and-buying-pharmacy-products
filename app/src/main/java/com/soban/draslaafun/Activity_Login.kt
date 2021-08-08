package com.soban.draslaafun

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Activity_Login : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        //configure progress dialogue
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        val edttxt_userEmail = findViewById<EditText>(R.id.edttxt_userEmail)
        val edttxt_userPassword = findViewById<EditText>(R.id.edttxt_userPassword)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val txt_createAccount = findViewById<TextView>(R.id.txt_createAccount)

        txt_createAccount.setOnClickListener {
            val intent = Intent(this, Activity_SignUp::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener {

            if (!Patterns.EMAIL_ADDRESS.matcher(edttxt_userEmail.text.toString()).matches()){
                edttxt_userEmail.setError("please enter valid email")
            }else if (TextUtils.isEmpty(edttxt_userPassword.text.toString())){
                edttxt_userPassword.setError("please enter password")
            }else{
                progressDialog.show()
                auth.signInWithEmailAndPassword(edttxt_userEmail.text.toString(),edttxt_userPassword.text.toString())
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this,"login successful",Toast.LENGTH_LONG).show()

                        ////starting new activity
                        startActivity(Intent(this,Activity_chatWithDoctor::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this,"login error, ${it.message}",Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}