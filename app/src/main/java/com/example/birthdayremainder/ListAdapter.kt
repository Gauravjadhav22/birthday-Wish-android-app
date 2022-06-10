package com.example.birthdayremainder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


import android.widget.Button

import android.widget.TextView
import android.widget.Toast

class ListAdapter(val context:Context,val list:ArrayList<PersonData>):BaseAdapter() {

    override fun getCount(): Int {
        //TODO("Not yet implemented")
        return list.size

    }

    override fun getItem(p0: Int): Any {
    //TODO("Not yet implemented")

        return p0
    }

    override fun getItemId(p0: Int): Long {
        // TODO("Not yet implemented")
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
    //TODO("Not yet implemented")
        val view:View=LayoutInflater.from(context).inflate(R.layout.row_layout,p2,false)

        val name =view.findViewById<TextView>(R.id.pname)
        val mobile = view.findViewById<TextView>(R.id.pmobile)
        val age = view.findViewById<TextView>(R.id.page)
        val btn = view.findViewById<TextView>(R.id.sndmsg)
//        val listView = view.findViewById<ListView>(R.id.listview)


        name.text=list[p0].name
        mobile.text=list[p0].contact
        btn.setText("SEND MESSAGE TO ${list[p0].name}")
        age.setText("${list[p0].name} turns ${list[p0].age} today!.. ")


        return  view
    }
}