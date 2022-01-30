package com.rezonans.Main

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rezonans.ApiRequests.GetReguest
import com.rezonans.ApiRequests.NetworkManager
import com.rezonans.Retrofit.AccessToken.Data
import com.rezonans.Retrofit.AccessToken.RezApi
import com.rezonans.Sites.SitesActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    internal val APP_PREFERENCES = "mysettings"
    private val APP_PREFERENCES_REFRESH_TOKEN = "refreshToken"
    private val APP_PREFERENCES_ACCESS_TOKEN = "accessToken"
    var refreshToken = ""
    var accessToken: String? = ""
    val listObject = ArrayList<ListObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.rezonans.R.layout.activity_main)

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        if (pref.contains(APP_PREFERENCES_REFRESH_TOKEN)) {refreshToken = pref.getString(APP_PREFERENCES_REFRESH_TOKEN, "null")}

        //проверка наличия интернета
        if (NetworkManager.isNetworkAvailable(this)) {
            // интернет соединение есть
            getAccessToken()

        } else {
            // если сети нет показываем Тост
            val myToast = Toast.makeText(this, "Нет активного соединения с интернетом", Toast.LENGTH_SHORT)
            myToast.show()
        }
    }

    fun getAccessToken() {
        var BASE_URL = "http://mx3.rez.ru:5000/api/"
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", ("Bearer " + refreshToken).toString().trim())
                .build()
            chain.proceed(newRequest)
        }.build()

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var api = retrofit.create(RezApi::class.java)
        var call = api.getData()

        call.enqueue(object : Callback<Data> {

            override fun onResponse(call: Call<Data>?, response: Response<Data>?) {
                var datares = response?.body()
                accessToken = datares?.access_token
                if (accessToken.toString() != "null") {
                    //сохраняем accessToken (нужно хранить и время, токен живет 15 мин)
                    var editor = pref.edit()
                    editor.putString(APP_PREFERENCES_ACCESS_TOKEN, accessToken)
                    editor.apply()

                    //getObject ()
                    var url = "http://mx3.rez.ru:5000/api/sites"
                    var par = ("Bearer " + accessToken).toString().trim()
                    handleJson(GetReguest().execute(url, par).get())
                } else {
                    test.text = "говно, accessToken = "+ accessToken
                }
            }

            override fun onFailure(call: Call<Data>?, t: Throwable?) {
                Log.v("Error", t.toString())
                test.text = "говно, запрос с ошибкой - "+ t.toString()
            }
        })
    }


    private fun handleJson(jsonString: String?) {
        var x = 0
        val JA = JSONArray(jsonString)
        while (x < JA.length()) {
            val JO = JA.getJSONObject(x)
            var JO_id = JO.getJSONObject("_id")
            listObject.add(
                ListObject(
                    JO_id.getString("\$oid"),
                    JO.getString("name"),
                    JO.getString("company"),
                    JO.getString("location")
                )
            )
            x++
        }

        val adapter = MainListViewAdapter(this, listObject)
        listViewSites.adapter = adapter

    }

    fun Detal(view: View) {
        val SitesIntent = Intent(this, SitesActivity::class.java)
        val lv = listViewSites
        val position = lv.getPositionForView(view)
        val id = listObject[position]._id
        val name = listObject[position].name
        val company = listObject[position].company
        val location = listObject[position].location
        SitesIntent.putExtra(SitesActivity.id_sites, id)
        SitesIntent.putExtra(SitesActivity.name_sites, name)
        SitesIntent.putExtra(SitesActivity.company_sites, company)
        SitesIntent.putExtra(SitesActivity.location_sites, location)
        startActivity(SitesIntent)
    }


}
