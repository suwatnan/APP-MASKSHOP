package com.example.maskshop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //clear data from share preference

        val sharePrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference,
            Context.MODE_PRIVATE
        )
        val editor = sharePrefer.edit()
        editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

        editor.commit() // ยืนยันการแก้ไข preferences

        //return to login page
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)

        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

}