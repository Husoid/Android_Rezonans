package com.rezonans.Sites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rezonans.ApiRequests.GetReguest
import com.rezonans.Devices.Bbox.Bbox00401Activity
import com.rezonans.Devices.Bbox.Bbox01602Activity
import com.rezonans.R
import kotlinx.android.synthetic.main.activity_sites.*
import org.json.JSONArray

class SitesActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    internal val APP_PREFERENCES = "mysettings"
    private val APP_PREFERENCES_ACCESS_TOKEN = "accessToken"
    var accessToken: String? = ""
    val dateSites = ArrayList<DataSites>()

    var idSites = ""
    var nameSites = ""
    var companySites = ""
    var locationSites = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sites)

        idSites = intent.getStringExtra(id_sites)
        nameSites = intent.getStringExtra(name_sites)
        companySites = intent.getStringExtra(company_sites)
        locationSites = intent.getStringExtra(location_sites)

        textViewName.text = nameSites
        textViewCompany.text = companySites
        textViewLocation.text = locationSites

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        if (pref.contains(APP_PREFERENCES_ACCESS_TOKEN)) {accessToken = pref.getString(APP_PREFERENCES_ACCESS_TOKEN, "null")}

        var url = "http://mx3.rez.ru:5000/api/sites/$idSites/devices"
        var par = ("Bearer " + accessToken).toString().trim()
        handleJson(GetReguest().execute(url, par).get())
        //textView6.text = GetReguest().execute(url, par).get()
    }
    companion object {
        const val id_sites = "idSites"
        const val name_sites = "nameSites"
        const val company_sites = "companySites"
        const val location_sites = "locationSites"
    }

    private fun handleJson(jsonString: String?) {
        var x = 0
        val JA = JSONArray(jsonString)
        while (x < JA.length()) {
            val JO = JA.getJSONObject(x)
            var JO_device_info = JO.getJSONObject("device_info")
            var JO_bbox00401 = JO.getJSONObject("bbox" + JO.getString("bbox_format"))
            var JO_basic_info = JO_bbox00401.getJSONObject("basic_info")
            var name =""
            try { name = JO.getString("name")} catch (e : Exception) {name = "Имя не задано"}
            dateSites.add(
                DataSites(
                    JO.getString("_id"),
                    JO.getString("bbox_format"),
                    JO_device_info.getString("part_number"),
                    JO_device_info.getString("device_number"),
                    JO_basic_info.getString("machine_name"),
                    JO_basic_info.getString("machine_number"),
                    name
                )
            )
            x++
        }

        val adapter = SitesListViewAdapter(this, dateSites)
        listViewDevice.adapter = adapter

    }

    fun Dev(view: View) {
        var SitesIntent = Intent(this, Bbox00401Activity::class.java)
        val lv = listViewDevice
        val position = lv.getPositionForView(view)
        val id = dateSites[position].id
        if (dateSites[position].bbox_format=="00401") {
            SitesIntent = Intent(this, Bbox00401Activity::class.java)
            SitesIntent.putExtra(Bbox00401Activity.id_dev, id)
            //textViewName.text = "00401"
        }
        if (dateSites[position].bbox_format=="01602") {
            SitesIntent = Intent(this, Bbox01602Activity::class.java)
            SitesIntent.putExtra(Bbox01602Activity.id_dev, id)
            //textViewName.text = "01602"
        }
        //val id = dateSites[position].id
        //SitesIntent.putExtra(Bbox00401Activity.id_dev, id)
        //textViewName.text = "Я тут"
        startActivity(SitesIntent)
    }

}
