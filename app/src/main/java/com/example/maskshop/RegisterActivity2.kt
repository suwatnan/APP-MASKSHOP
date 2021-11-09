package com.example.maskshop

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RegisterActivity2 : AppCompatActivity() {
    var editTextFirstName: EditText? = null
    var editTextLastName: EditText? = null
    var editTextUserName: EditText? = null
    var editTextPassword: EditText? = null
    var editTextAddress: EditText? = null
    var editTextPhone: EditText? = null
    var editTextGmail: EditText? = null
    var btnUpdate: Button? = null
    var image1: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        image1 = findViewById(R.id.imageButton1)
        image1?.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //find to widgets on a layout
        editTextUserName = findViewById(R.id.editTextUserName)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextGmail = findViewById(R.id.editTextGmail)
        btnUpdate = findViewById(R.id.btnUpdate)


        btnUpdate!!.setOnClickListener {
            register()
        }
    }
        private fun register()
        {
            var url: String = getString(R.string.root_url) + getString(R.string.user_url)
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()
                .add("username", editTextUserName?.text.toString())
                .add("password", editTextPassword?.text.toString())
                .add("firstname", editTextFirstName?.text.toString())
                .add("lastname", editTextLastName?.text.toString())
                .add("address",  editTextAddress?.text.toString())
                .add("phone", editTextPhone?.text.toString())
                .add("gmail", editTextGmail?.text.toString())
                .add("userTypeID", "1")
                .build()

            val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val data = JSONObject(response.body!!.string())
                        if (data.length() > 0) {
                            Toast.makeText(this, "สมัครสมาชิกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    response.code
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
    }

}