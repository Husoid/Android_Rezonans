package com.rezonans.Devices.Bbox

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.rezonans.ApiRequests.GetReguest
import com.rezonans.R.layout.activity_bbox01602
import kotlinx.android.synthetic.main.activity_bbox00401.*
import org.json.JSONObject


class Bbox01602Activity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    internal val APP_PREFERENCES = "mysettings"
    private val APP_PREFERENCES_ACCESS_TOKEN = "accessToken"
    var accessToken: String? = ""

    var idDev = ""

    var url = ""
    var par = ""

    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_bbox01602)

        idDev = intent.getStringExtra(id_dev)

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        if (pref.contains(APP_PREFERENCES_ACCESS_TOKEN)) {accessToken = pref.getString(APP_PREFERENCES_ACCESS_TOKEN, "null")}

        url = "http://mx3.rez.ru:5000/api/devices/$idDev"
        par = ("Bearer " + accessToken).toString().trim()

        mHandler.removeCallbacks(paramUpdater)
        mHandler.postDelayed(paramUpdater, 100)



       // textView_datetime.text = "Еще не готово для 01602"
        handleJson(GetReguest().execute(url, par).get())
    }


    private val paramUpdater = object : Runnable {
        override fun run() {
            handleJson(GetReguest().execute(url, par).get())
            mHandler.postDelayed(this, 1000)
        }
    }



    companion object {
        const val id_dev = "idDev"
    }

    private fun handleJson(jsonString: String?) {
        val JO = JSONObject(jsonString)
        var JO_bbox = JO.getJSONObject("bbox"+ JO.getString("bbox_format"))
        var JO_tpchr = JO_bbox.getJSONObject("tpchr")
        //var JO_kran_optime = JO_bbox00401.getJSONObject("kran_optime")
       // var JO_max_val = JO_bbox00401.getJSONObject("max_val")

        //ПАРАМЕТРЫ THCPR
        // дата и время записи
//        textView_datetime.text = JO_tpchr.getString("pack_date")
        //угол наклона стрелы
        textView_ugstr.text = JO_tpchr.getDouble("ugstr").toString()
        //Признак режима настройки
        if (JO_tpchr.getDouble("install") > 0) {textView_nastroika.setTextColor(resources.getColor(
            com.rezonans.R.color.colorLogo))} else {textView_nastroika.setTextColor(resources.getColor(
            com.rezonans.R.color.colorZnak))}
        //Длинная стрелы
        textView_l.text = JO_tpchr.getDouble("l").toString()
        //признак ускоренной работы лебедки
        //JO_tpchr.getDouble("fast_leb")
        //Признак фиксации секций стрелы
        //JO_tpchr.getDouble("strfix_hand")
        //Угол поворота платформы, градусов
        textView_ugol.text = JO_tpchr.getDouble("ugaz").toString()
        //Индекс опорного контура
        //JO_tpchr.getDouble("kontur")
        //Индекс рабочей зоны по углу поворота
        //JO_tpchr.getDouble("zona")
        //Блокировка приближения к ЛЭП
        //JO_tpchr.getDouble("lep")
        //Блокировка по отказу датчика
        //if (JO_tpchr.getDouble("dats") > 0) {textView_zap_regim.setTextColor(resources.getColor(com.rezonans.R.color.colorLogo))} else {textView_zap_regim.setTextColor(resources.getColor(
        //    com.rezonans.R.color.colorZnak))}
        //Нажатие кнопки обхода блокировки на блоке индикации
        if (JO_tpchr.getDouble("operatorintervent") > 0) {textView_operator.setTextColor(resources.getColor(
            com.rezonans.R.color.colorLogo))} else {textView_operator.setTextColor(resources.getColor(
            com.rezonans.R.color.colorZnak))}
        //Поршневое давление, атм
        //JO_tpchr.getDouble("pp")
        //Состояние выхода 1
        //JO_tpchr.getDouble("out1")
        //Штоковое давление, атм
        //JO_tpchr.getDouble("psh")
        //Состояние выхода 2
        //JO_tpchr.getDouble("out2")
        //Масса груза на крюке, т
        textView_q.text = JO_tpchr.getDouble("q").toString()
        //Вылет, м
        textView_r.text = JO_tpchr.getDouble("r").toString()
        //Высота подъёма стрелы, м
        textView_h.text = JO_tpchr.getDouble("h").toString()
        //Допустимая масса груза, т
        textView_qm.text = JO_tpchr.getDouble("qm").toString()
        //Момент загрузки, %
        if (JO_tpchr.getDouble("m")>120) {progressBar.progress = 120} else {progressBar.progress = JO_tpchr.getDouble("m").toInt()}
        //Кратность полиспаста крюка
        //JO_tpchr.getDouble("pol")
        //Индекс гуська
        //JO_tpchr.getDouble("nstr")
        //Признак подъема стрелы
        if (JO_tpchr.getDouble("op_strela_up")>0) {
            textView_BOOM_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_BOOM_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_BOOM_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_BOOM_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак опускания стрелы
        if (JO_tpchr.getDouble("op_strela_down") >0) {
            textView_BOOM_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_BOOM_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_BOOM_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_BOOM_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак выдвижения стрелы
        if (JO_tpchr.getDouble("op_tel_up") >0) {
            textView_TEL_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_TEL_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_TEL_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_TEL_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак втягивания стрелы
        if (JO_tpchr.getDouble("op_tel_down") >0) {
            textView_TEL_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_TEL_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_TEL_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_TEL_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак подъёма крюка
        if (JO_tpchr.getDouble("op_hook_up") >0) {
            textView_HOOK1_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_HOOK1_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_HOOK1_UP.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_HOOK1_UP.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак опускания крюка
        if (JO_tpchr.getDouble("op_hook_down") >0) {
            textView_HOOK1_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorLogo))
            textView_HOOK1_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorBack))
        } else {
            textView_HOOK1_DOWN.setBackgroundColor(resources.getColor(com.rezonans.R.color.colorBack))
            textView_HOOK1_DOWN.setTextColor(resources.getColor(com.rezonans.R.color.colorZnak))
        }
        //Признак поворота вправо
        //JO_tpchr.getDouble("op_turn_right")
        //Признак поворота влево
        //JO_tpchr.getDouble("op_turn_left")
        //Блокировка координатной защиты "Высота"
        //JO_tpchr.getDouble("blk_ceiling")
        //Блокировка координатной защиты "Стена"
        //JO_tpchr.getDouble("blk_wall")
        //Блокировка координатной защиты "Поворот вправо"
        //JO_tpchr.getDouble("blk_right")
        //Блокировка координатной защиты "Поворот влево"
        //JO_tpchr.getDouble("blk_left")
        //Блокировка по минимальному вылету
        if (JO_tpchr.getDouble("blk_radius_min") >0) {textView_min_vilet.setVisibility(View.VISIBLE)} else {textView_min_vilet.setVisibility(View.GONE)}
        //Блокировка по максимальному вылету
        if (JO_tpchr.getDouble("blk_radius_max") >0) {textView_max_vilet.setVisibility(View.VISIBLE)} else {textView_max_vilet.setVisibility(View.GONE)}
        //Блокировка по подъёму крюка
        //JO_tpchr.getDouble("blk_hook_up")
        //Блокировка по сматыванию каната с грузовой лебёдки
        //JO_tpchr.getDouble("blk_hook_down")


    }
}
