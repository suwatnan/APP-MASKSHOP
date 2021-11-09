package com.example.maskshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    var btnregis: Button? = null
    var btnLogin:Button?= null
    var btnview:Button?= null
    val appPreference:String = "appPrefer"
    val userIdPreference:String = "userIdPref"
    val usernamePreference:String = "usernamePref"
    val userTypePreference:String = "userTypePref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        btnregis = findViewById(R.id.btnreg)
        btnLogin = findViewById(R.id.btnLogin)
        btnview =  findViewById(R.id.btnview)
            btnLogin?.setOnClickListener {

                //Find to components on a layout
                val editUsername = findViewById<EditText>(R.id.editTextUserName)
                val editPassword = findViewById<EditText>(R.id.editTextPassword)
                Log.d("txt","x1")
                val url = getString(R.string.root_url) + getString(R.string.login_url)
                val okHttpClient = OkHttpClient()
                val formBody: RequestBody = FormBody.Builder()
                        .add("username", editUsername.text.toString())
                        .add("password", editPassword.text.toString())
                        .build()
                val request: Request = Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        try {
                            Log.d("txt","x2")
                            val obj = JSONObject(response.body!!.string())
                            val userID = obj["userID"].toString()
                            val username = obj["username"].toString()
                            val userTypeID = obj["usertypeID"].toString()


                            //Create shared preference to store user data
                            val sharedPrefer: SharedPreferences =
                                    getSharedPreferences(appPreference, Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPrefer.edit()

                            editor.putString(userIdPreference, userID)
                            editor.putString(usernamePreference, username)
                            editor.putString(userTypePreference, userTypeID)
                            editor.commit()
                            Log.d("txt","x3")
                            //return to login page
                            if (userTypeID == "1") //1 = general users
                            {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (userTypeID == "2")//2 = admin
                            {
                                val intent = Intent(applicationContext, AdminActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            Log.d("txt","x4")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        response.code
                        Toast.makeText(applicationContext, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

        }
        btnregis?.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity2::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(applicationContext, "สมัครสมาชิก", Toast.LENGTH_LONG).show()
        }
        btnview?.setOnClickListener {
            val intent = Intent(applicationContext, ViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
        override fun onResume() {
            val sharedPrefer: SharedPreferences =
                    getSharedPreferences(appPreference, Context.MODE_PRIVATE)
            val usertype = sharedPrefer?.getString(userTypePreference, null)

            //if (sharedPrefer.contains(usernamePreference))
            if (usertype=="1") {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            else if(usertype=="2")
            {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            super.onResume()
        }
    }
