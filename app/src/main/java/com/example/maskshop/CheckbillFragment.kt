package com.example.maskshop

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat

class CheckbillFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var proid = ArrayList<String>()
    var stokUp = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_checkbill, container, false)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = root.findViewById(R.id.recyclerView)
        showDataList()
        return root
    }

    //show a data list
    private fun showDataList() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewpayment_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add( Data(
                                item.getString("paymentID"),
                                item.getString("orderID"),
                                item.getString("imagebill")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data(
        var paymentID: String, var orderID: String, var imagebill: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_bill,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.product_image_url) + data.imagebill
            Picasso.get().load(url).into(holder.imagebill)
            holder.orderID.text=data.orderID
            holder.imageBtncon.setOnClickListener {

                updatestatus()

            }
            holder.imageBtncal.setOnClickListener {
                updatestatus2()
            }

        }



        private fun updatestatus2() {

            var url: String =
                getString(R.string.root_url) + getString(R.string.updatestatus_url) + viewIDOrder()
            Log.d("log",url)
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                .add("status", "3")
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()

            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val data = JSONObject(response.body!!.string())
                        if (data.length() > 0) {
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



        private fun updatestatus() {

            var url: String =
                getString(R.string.root_url) + getString(R.string.updatestatus_url) + viewIDOrder()
            Log.d("log",url)
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                .add("status", "0")
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()

            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val data = JSONObject(response.body!!.string())
                        if (data.length() > 0) {
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
        private fun viewIDOrder(): String?
        {
            var id :String? = null
            var url: String = getString(R.string.root_url) + getString(R.string.SelectIDOrder_url)
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

                            id = data.getString("orderID")

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
            return id
        }
        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var orderID: TextView = itemView.findViewById(R.id.txtIdorder)
            var imagebill: ImageView = itemView.findViewById(R.id.ImageFileName)
            var imageBtncon: ImageButton = itemView.findViewById(R.id.imageBtncon)
            var imageBtncal: ImageButton = itemView.findViewById(R.id.imageBtncal)
        }
    }

}