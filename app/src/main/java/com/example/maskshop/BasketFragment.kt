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
import java.text.DecimalFormat
import java.text.NumberFormat


class BasketFragment : Fragment() {
    var userID: String? = null
    var recyclerView: RecyclerView? = null
    var btnconfirm:Button? =null
    var txtprice:TextView? =null

    var basketID: String? = null
    var x: Int = 0
    var q: Int = 0
    var s: Int = 0

    private val client = OkHttpClient()
    private val datadetail = ArrayList<Datadetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_basket, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)

        recyclerView = root.findViewById(R.id.recyclerView)
        txtprice = root.findViewById(R.id.txtprice)

        btnconfirm = root.findViewById(R.id.btnUpload)

        btnconfirm?.setOnClickListener {
            AddOrder()

            showDataList()
            for (i in datadetail){
                AddOrderDetail(i.productID,i.basketnum,i.totalprice)
                var stock = Integer.valueOf(i.queryproduct)-Integer.valueOf(i.basketnum)
                updatestok(i.productID,stock.toString())

                Log.d("eee",i.productID+i.basketnum+i.totalprice)
            }
            //uporder()
            deletebasketaoto()
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        showDataList()
        return  root

    }

   private fun updatestok(productID: String,stokUp:String)
    {
            var url: String =
                getString(R.string.root_url) + getString(R.string.updatestok_url) + productID
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                .add("quantity", stokUp)
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()

            try {
                Log.d("log", url)
                Log.d("log", "x3")
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
    private fun uporder()
    {
            var url: String =
                getString(R.string.root_url) + getString(R.string.orderupdate_url) + viewIDOrder()
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                .add("status", "1")
                .add("userID", userID.toString())
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()

            try {
                Log.d("log", url)
                Log.d("log", "x3")
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

    private fun AddOrderDetail(productID: String,quantity:String,price:String)
    {

                var url: String = getString(R.string.root_url) + getString(R.string.AddOrderDetail_url)
                val okHttpClient = OkHttpClient()
                val formBody: RequestBody = FormBody.Builder()
                        .add("productID", productID)
                        .add("quantity",quantity)
                        .add("price", price)
                        .add("orderID", viewIDOrder().toString())
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
    private fun AddOrder()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.addorderna_url)+userID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
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
        datadetail.clear()
        x=0
        val url: String = getString(R.string.root_url) + getString(R.string.viewcart) + userID.toString()
        Log.d("txt",url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add(Data(
                                item.getString("basketID"),
                                item.getString("basketnum"),
                                item.getString("userID"),
                                item.getString("productID"),
                                item.getString("total"),
                                item.getString("quantity"),
                                item.getString("imageFileName")
                            )
                            )
                            datadetail.add(Datadetail(
                                    item.getString("basketID"),
                                    item.getString("basketnum"),
                                    item.getString("userID"),
                                    item.getString("productID"),
                                    item.getString("total"),
                                    item.getString("quantity"),
                                    item.getString("imageFileName")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                            x+=Integer.valueOf(item.getString("total"))

                     //   stock.add(item.getString("quantity"))
                        }
                        txtprice?.text=  (x).toString()


                    } else {
                        recyclerView!!.adapter = DataAdapter(data)
                        txtprice?.text="0"
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
        var basketID: String, var basketnum: String,
        var userID: String, var productID: String,var totalprice: String,var queryproduct: String,var image: String
    )
    internal class Datadetail(
            var basketID: String, var basketnum: String,
            var userID: String, var productID: String,var totalprice: String,var queryproduct: String,var image: String
    )


    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("txt","x2")
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_orderbasket,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("txt","x3")
            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) +
                    getString(R.string.product_image_url) + data.image
            Picasso.get().load(url).into(holder.img)
            holder.textProductName.text ="สินค้า : " +data.productID
            holder.textquantity.text ="จำนวน : " +data.basketnum
            holder.textPrice.text ="รวม: " +data.totalprice
            //x += Integer.valueOf(data.totalprice)
            //txtprice?.text = x.toString()
            q += Integer.valueOf(data.basketnum)


            holder.btndelete.setOnClickListener {
                deletebasket(data.basketID)
                x-=Integer.valueOf(data.totalprice)
                txtprice?.text = x.toString()
                showDataList()
            }


        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var textProductName: TextView = itemView.findViewById(R.id.textProductName)
            var textquantity: TextView = itemView.findViewById(R.id.textquantity)
            var textPrice: TextView = itemView.findViewById(R.id.textPrice)
            var img: ImageView = itemView.findViewById(R.id.imageProduct)
            var btndelete : ImageButton= itemView.findViewById(R.id.btnDelete)


        }

    }

    private fun deletebasket(basketID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.deletecart) + basketID

        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .delete()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Toast.makeText(context, "ลบสินค้าที่เลือกแล้ว", Toast.LENGTH_LONG).show()
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

    private fun deletebasketaoto() {
            var url: String =
                getString(R.string.root_url) + getString(R.string.deleteall_url) +userID

            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .delete()
                .build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {


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


