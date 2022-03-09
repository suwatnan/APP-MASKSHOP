package com.example.maskshop

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class UserFragment : Fragment() {
    var imageView1: ImageView? = null
    var txtFirstName: TextView? = null
    var txtLastName: TextView? = null
    var textViewAddress: TextView? = null
    var textViewPhone: TextView? = null
    var textViewGmail: TextView? = null
    var userID:String?=null;
    var btnUpdate: Button? = null
    var btnDelete: Button? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        btnUpdate = root.findViewById(R.id.btnUpdate)
        btnUpdate?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userID", userID)
            val fm = UserUpdateFragment()
            fm.arguments = bundle;
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, fm)
            fragmentTransaction.commit()
        }

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)

        //find to widgets on a layout
        imageView1 = root.findViewById(R.id.imageView1)
        txtFirstName = root.findViewById(R.id.txtFirstName)
        txtLastName = root.findViewById(R.id.txtLastName)
        textViewAddress = root.findViewById(R.id.textViewAddress)
        textViewPhone = root.findViewById(R.id.textViewPhone)
        textViewGmail = root.findViewById(R.id.textViewGmail)
        btnUpdate = root.findViewById(R.id.btnUpdate)
        btnDelete = root.findViewById(R.id.btnDelete)

        viewUser(userID)

        return root
    }
     private fun viewUser(userID: String?)
     {
         var url: String = getString(R.string.root_url) + getString(R.string.user_url) + userID
         val okHttpClient = OkHttpClient()
         val request: Request = Request.Builder()
                 .url(url)
                 .get()
                 .build()

         try {
             val response = okHttpClient.newCall(request).execute()
             if (response.isSuccessful) {
                 try {
                     val data = JSONObject(response.body!!.string())
                     if (data.length() > 0) {

                         var imgUrl = getString(R.string.root_url) +
                                 getString(R.string.user_image_url) +
                                 data.getString("imageFileName")

                         Picasso.get().load(imgUrl).into(imageView1)
                         txtFirstName?.text = data.getString("firstname")
                         txtLastName?.text = data.getString("lastname")
                         textViewAddress?.text = data.getString("address")
                         textViewPhone?.text = data.getString("phone")
                         textViewGmail?.text = data.getString("gmail")


                         btnUpdate!!.setOnClickListener {
                             val bundle = Bundle()
                             bundle.putString("userID", userID)

                             val fm = UserUpdateFragment()
                             fm.arguments = bundle;

                             val fragmentTransaction = requireActivity().
                             supportFragmentManager.beginTransaction()
                             fragmentTransaction.addToBackStack(null)
                             fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                             fragmentTransaction.commit()
                         }

                         btnDelete!!.setOnClickListener {
                             deleteUser(userID)
                             val fragmentTransaction = requireActivity().
                             supportFragmentManager.beginTransaction()
                             fragmentTransaction.replace(R.id.nav_host_fragment, LogoutFragment())
                             fragmentTransaction.addToBackStack(null)
                             fragmentTransaction.commit()
                         }
                         /*
                         txtLineID!!.setOnClickListener {

                             //show dialog with qr code
                             val builder = AlertDialog.Builder(it.context)
                             val inflater = layoutInflater
                             val dialogLayout: View = inflater.inflate(R.layout.qrcode, null)
                             builder.setPositiveButton("OK", null)
                             builder.setView(dialogLayout)
                             builder.show()
                         }*/


                         // val fragmentTransaction = fragmentManager!!.beginTransaction()


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
    private fun deleteUser(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.user_url) + userID

        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .delete()
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                    }

                } catch (e: JSONException) {
                    Toast.makeText(context, "ยกเลิกการสมัครสมาชิกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            } else {
                response.code
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
//test
}

