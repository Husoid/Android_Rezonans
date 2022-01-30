package com.rezonans.Sites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.rezonans.R
import java.util.ArrayList

class SitesListViewAdapter (val context: Context, val list: ArrayList<DataSites>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.sites_row_listview,parent,false )

        val part_number = view.findViewById(R.id.textView_part_number) as AppCompatTextView
        val device_number = view.findViewById(R.id.textView_device_number) as AppCompatTextView
        val machine_name = view.findViewById(R.id.textView_machine_name) as AppCompatTextView
        val machine_number = view.findViewById(R.id.textView_machine_number) as AppCompatTextView
        val name = view.findViewById(R.id.textViewNameDev) as AppCompatTextView

        part_number.text = list[position].part_number
        device_number.text = list[position].device_number
        machine_name.text = list[position].machine_name
        machine_number.text = list[position].machine_number
        name.text = list[position].name + " " + list[position].bbox_format

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