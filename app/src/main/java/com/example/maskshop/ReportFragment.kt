package com.example.maskshop

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class ReportFragment : Fragment() {
    var userID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_report, container, false)

        //get shared preference
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)

        //handle error from using SyncHttpClient
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val monthlySale: BarChart = root.findViewById(R.id.monthlySale)
        val topFiveProduct: PieChart = root.findViewById(R.id.topFiveProduct)

        showMonthlySale(monthlySale)
        showTopFiveProduct(topFiveProduct)

        return root
    }
    private fun showMonthlySale(chart: BarChart) {
        Log.d("txt","x0")
        val dataSets = ArrayList<IBarDataSet>()
        val entries = ArrayList<BarEntry>()
        val labels = arrayListOf<String>()
        val months = arrayOf(
            "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
            "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
        )

        //Load data from API
        var url: String = getString(R.string.root_url) + getString(R.string.monthlySale_url)
        //url += "$userID"
        Log.d("x1",url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            var index = java.lang.Float.valueOf(i.toString())
                            var value = java.lang.Float.valueOf(item.getString("totalAmount"))
                            entries.add(BarEntry(index, value))
                            labels.add(months[item.getString("month").toInt() - 1])
                        }
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

        val dataset = BarDataSet(entries, "")
        dataset.valueTextSize = 12f
        dataset.setColors(*ColorTemplate.COLORFUL_COLORS) // set the color
        dataset.valueFormatter = MyValueFormatter("###,###,###,##0.0", "")
        dataSets.add(dataset) //Data set to data
        val data = BarData(dataSets)
        chart.data = data

        //chart.getXAxis().setLabelRotationAngle(0);
        chart.description.isEnabled = false //ซ่อนคำว่า "Description Label"
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        //Hide grid line
        chart.xAxis.setDrawGridLines(false)
        val xAxis = chart.xAxis
        xAxis.labelCount = labels!!.count()
        xAxis.textSize = 12f
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        val LeftAxis = chart.axisLeft
        LeftAxis.textSize = 12f
        val RightAxis = chart.axisRight
        RightAxis.textSize = 12f
        RightAxis.isEnabled = false //กำหนดให้ตัวเลขด้านขวาไม่ต้องแสดง

        //Define legend
        val legend = chart.legend
        legend.isEnabled = false
    }

    private fun showTopFiveProduct(chart: PieChart)
    {
        val entries = ArrayList<PieEntry>()
        //Load data from API
        var url: String = getString(R.string.root_url) + getString(R.string.topFiveProduct_url)
       //url += "$userID"
        Log.d("x2",url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            var name = item.getString("productName")
                            var value = java.lang.Float.valueOf(
                                item.getString("totalAmount")
                            )
                            entries.add(PieEntry(value, name))
                        }
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

        //Entity to data set
        val dataset = PieDataSet(entries, "")
        dataset.selectionShift = 10f
        dataset.valueTextSize = 12f
        dataset.valueTextColor = Color.WHITE
        dataset.setColors(*ColorTemplate.COLORFUL_COLORS) // set the color


        //Data set to data

        //Data set to data
        val data = PieData(dataset)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart.holeRadius = 30f
        chart.transparentCircleRadius = 40f //=40-30

        chart.setEntryLabelColor(Color.WHITE)
        chart.centerText = "ยอดสั่งซื้อ"

        //Define animation

        //Define animation
        chart.animateY(3000)

        //Define inside/outside slide

        //Define inside/outside slide
        dataset.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        dataset.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        dataset.valueLinePart1Length = 0.5f
        dataset.valueLinePart2Length = 0.5f

        //Show percent

        //Show percent
        chart.setUsePercentValues(false)
        dataset.valueFormatter = PercentFormatter()

        //Define legend

        //Define legend
        val legend = chart.legend
        legend.isEnabled = false

        //Hide text

        //Hide text
        chart.description.isEnabled = false //ซ่อนคำว่า "Description Label"


    }

}