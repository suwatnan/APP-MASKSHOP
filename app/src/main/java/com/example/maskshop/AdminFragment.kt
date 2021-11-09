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
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class AdminFragment : Fragment() {
    var imageView1: ImageView? = null
    var txtFirstName: TextView? = null
    var txtLastName: TextView? = null
    var textViewAddress: TextView? = null
    var textViewPhone: TextView? = null
    var textViewGmail: TextView? = null
    var userID:String?=null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_admin, container, false)

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