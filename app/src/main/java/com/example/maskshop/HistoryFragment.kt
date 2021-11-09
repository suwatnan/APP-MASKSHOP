package com.example.maskshop

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat

class HistoryFragment : Fragment() {
    var userID: String? = null
    var x: Int = 0
    var y: Int = 0
    var recyclerView: RecyclerView? = null
    var imageButton8:ImageButton? = null
    private val client = OkHttpClient()
    private val data = ArrayList<Data>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)
        //List data
        recyclerView = root.findViewById(R.id.recyclerView)





        showDataList()
        return root
    }
    private fun showDataList() {
        Log.d("txt","x1")
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.orderviiiiiii_url) +
                userID.toString()

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
                        val status = arrayOf(
                                "กำลังจัดส่ง", "คลิกชำระเงิน", "ตรวจสอบ", "ชำระเงินไม่สำเร็จ")
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add( Data(
                                    item.getString("orderID"),
                                    item.getString("updated_at"),
                                    item.getString("total"),
                                    item.getString("totalquantity"),
                                    status[item.getInt("status")]
                            )
                            )
                        }
                        recyclerView!!.adapter = DataAdapter(data)
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

    internal class Data(
            var orderID: String, var orderDate: String,
            var totalPrice: String,var totalQuantity: String, var orderStatus: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("txt","x2")
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_orderhistory,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("txt","x3")
            val data = list[position]
            holder.data = data
            holder.txtOrderID.text = data.orderID
            holder.txtOrderDate.text = data.orderDate
            if(Integer.valueOf(data.totalQuantity) > 199){
                x = (Integer.valueOf(data.totalPrice) * 20 )/ 100
                holder.txtTotalPrice.text = (Integer.valueOf(data.totalPrice) - x).toString()
                y = Integer.valueOf(data.totalPrice) - x
                holder.itemView.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("y", y.toString())

                    val fm = PaymentFragment()
                    fm.arguments = bundle
                    val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransition.addToBackStack(null)
                    fragmentTransition.replace(R.id.nav_host_fragment,fm)
                    fragmentTransition.commit()
                }
            }else if (Integer.valueOf(data.totalQuantity) > 99){
                x = (Integer.valueOf(data.totalPrice) * 10 )/ 100
                holder.txtTotalPrice.text = (Integer.valueOf(data.totalPrice) - x).toString()
                y = Integer.valueOf(data.totalPrice) - x
                holder.itemView.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("y", y.toString())

                    val fm = PaymentFragment()
                    fm.arguments = bundle
                    val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransition.addToBackStack(null)
                    fragmentTransition.replace(R.id.nav_host_fragment,fm)
                    fragmentTransition.commit()
                }
            }else  if(Integer.valueOf(data.totalQuantity) > 49){
                x = (Integer.valueOf(data.totalPrice) * 5 )/ 100
                holder.txtTotalPrice.text = (Integer.valueOf(data.totalPrice) - x).toString()
                y = Integer.valueOf(data.totalPrice) - x
                holder.itemView.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("y", y.toString())

                    val fm = PaymentFragment()
                    fm.arguments = bundle
                    val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransition.addToBackStack(null)
                    fragmentTransition.replace(R.id.nav_host_fragment,fm)
                    fragmentTransition.commit()
                }
            }else if(Integer.valueOf(data.totalQuantity) < 49){

                holder.txtTotalPrice.text = data.totalPrice
                y = Integer.valueOf(data.totalPrice)
                holder.itemView.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("y", y.toString())

                    val fm = PaymentFragment()
                    fm.arguments = bundle
                    val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransition.addToBackStack(null)
                    fragmentTransition.replace(R.id.nav_host_fragment,fm)
                    fragmentTransition.commit()
                }
            }

            holder.txtOrderStatus.text = data.orderStatus

        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtOrderID: TextView = itemView.findViewById(R.id.txtOrderID)
            var txtOrderDate: TextView = itemView.findViewById(R.id.txtOrderDate)
            var txtTotalPrice: TextView = itemView.findViewById(R.id.txtTotalPrice)
            var txtOrderStatus: TextView = itemView.findViewById(R.id.txtOrderStatus)

        }
    }
}
