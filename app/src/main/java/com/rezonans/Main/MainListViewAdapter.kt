package com.rezonans.Main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.rezonans.R
import java.util.*

class MainListViewAdapter (val context: Context, val list: ArrayList<ListObject>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.main_row_recycler,parent,false )

        val name = view.findViewById(R.id.nameSites) as AppCompatTextView
        val company = view.findViewById(R.id.companySites) as AppCompatTextView
        val location = view.findViewById(R.id.locationSites) as AppCompatTextView

        name.text = list[position].name
        company.text = list[position].company
        location.text = list[position].location

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}