package com.example.maskshop

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class UserUpdateFragment : Fragment() {
    var editTextFirstName: EditText? = null
    var editTextLastName: EditText? = null
    var editTextPassword: EditText? = null
    var editTextAddress: EditText? = null
    var editTextPhone: EditText? = null
    var editTextGmail: EditText? = null
    var btnUpdate: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user_update, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val bundle = this.arguments

        //find to widgets on a layout
        editTextFirstName = root.findViewById(R.id.editTextFirstName)
        editTextLastName = root.findViewById(R.id.editTextLastName)
        editTextPassword = root.findViewById(R.id.editTextPassword)
        editTextAddress = root.findViewById(R.id.editTextAddress)
        editTextPhone = root.findViewById(R.id.editTextPhone)
        editTextGmail = root.findViewById(R.id.editTextGmail)
        btnUpdate = root.findViewById(R.id.btnUpdate)


        btnUpdate!!.setOnClickListener {
            updateUser(bundle?.get("userID").toString())
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, UserFragment())
            fragmentTransaction.commit()
        }

        viewUser(bundle?.get("userID").toString())

        return root;
    }

    private fun viewUser(userID: String?) {
        var url: String = getString(R.string.root_url) + getString(R.string.user_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()

        try {
            Log.d("log", "x1")
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        editTextFirstName?.setText(data.getString("firstname"))
                        editTextLastName?.setText(data.getString("lastname"))
                        editTextPassword?.setText( data.getString("password"))
                        editTextAddress?.setText(data.getString("address"))
                        editTextPhone?.setText(data.getString("phone"))
                        editTextGmail?.setText(data.getString("gmail"))

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
    private fun updateUser(userID: String?)
    {
        Log.d("log", "x2")
        var url: String = getString(R.string.root_url) + getString(R.string.user_url) + userID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()

                .add("firstname", editTextFirstName?.text.toString())
                .add("lastname", editTextLastName?.text.toString())
                .add("password", editTextPassword?.text.toString())
                .add("address", editTextAddress?.text.toString())
                .add("phone", editTextPhone?.text.toString())
                .add("gmail", editTextGmail?.text.toString())
                .add("usertypeID", "1")
                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

        try {
            Log.d("log", url)
            Log.d("log", "x3")
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(context, "แก้ไขข้อมูลรสมาชิกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
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