package com.example.maskshop

import android.content.Context
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


class HomeFragment : Fragment() {
    var imgImageFileName: EditText? = null
    var txtProductName: EditText? = null
    var editQuantity: EditText? = null
    var btnAddToCart: EditText? = null
    var userID:String?=null;
    var btncart: ImageButton?=null;
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_home, container, false)


        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)


        val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = root.findViewById(R.id.recyclerView)
        btncart = root.findViewById(R.id.imageButton)
        btncart?.setOnClickListener {
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, BasketFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        showDataList()

        return root
    }
    private fun addtoCart(productID:String,basketnum:String)
    {
       // Log.d("txt","ff")
        var url: String = getString(R.string.root_url) + getString(R.string.addcart)
       // Log.d("txt",url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("userID", userID.toString())
            .add("productID", productID)
            .add("basketnum", basketnum)

            .build()
       // Log.d("txt","ff2")
        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("txt","ff3")
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

    private fun showDataList() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.product_url)
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
                            data.add(Data(
                                    item.getString("productID"),
                                    item.getString("productName"),
                                    item.getString("price"),
                                    item.getString("quantity"),
                                    item.getString("imageFileName")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG).show()
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

    internal class Data(
            var productID: String, var productName: String, var price: String,
            var quantity: String, var imageFileName: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_product,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) +
                    getString(R.string.product_image_url) + data.imageFileName
            Picasso.get().load(url).into(holder.imageFileName)
            holder.productName.text = data.productName
            holder.price.text = "\u0E3F" + data.price


            holder.removeProduct.setOnClickListener {
                var lQuantity =
                        Integer.valueOf(holder.quantity.text.toString())
                lQuantity -= 1
                if (lQuantity == 0) lQuantity = 1
                holder.quantity.setText(lQuantity.toString())
            }

            holder.addProduct.setOnClickListener {
                var lQuantity =
                        Integer.valueOf(holder.quantity.text.toString())
                lQuantity += 1
                holder.quantity.setText(lQuantity.toString())
            }

            holder.addToCart.setOnClickListener {
                addtoCart(data.productID,holder.quantity.text.toString())
                Toast.makeText(context, "คุณเลือก " + holder.productName.text, Toast.LENGTH_LONG).show()


            }

        }
        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var productName: TextView = itemView.findViewById(R.id.txtIdorder)
            var price: TextView = itemView.findViewById(R.id.txtPrice)
            var quantity: EditText = itemView.findViewById(R.id.editQuantity)
            var removeProduct: Button = itemView.findViewById(R.id.btnRemoveProduct)
            var addProduct: Button = itemView.findViewById(R.id.btnAddProduct)
            var addToCart: ImageButton = itemView.findViewById(R.id.btnAddToCart)
            var imageFileName: ImageView = itemView.findViewById(R.id.ImageFileName)
        }
    }
}