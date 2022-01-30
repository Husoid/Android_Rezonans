package com.rezonans.Logon

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.rezonans.Main.MainActivity
import com.rezonans.R
import kotlinx.android.synthetic.main.activity_logon.*

class LogonActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    internal val APP_PREFERENCES = "mysettings"
    private val APP_PREFERENCES_REFRESH_TOKEN = "refreshToken"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logon)

        //проверка запуска программы
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val q = pref.getString(APP_PREFERENCES_REFRESH_TOKEN, "null")
        if ((pref.contains(APP_PREFERENCES_REFRESH_TOKEN)) and (q !== "null")) {
            // не первый запуск QR считан и сохранен
            val startIntent = Intent(this, MainActivity::class.java)
            startActivity(startIntent)
            finish()
        } else {
            // первый запуск
            showDialogStart(this, "Это ваш первый запуск приложение.", "Необходимо отсканировать QRcode в личном кабинете", "Сканировать QRcode?", "Выйти").show()
        }
    }

    private fun showDialogStart(act: Activity, title: CharSequence, message: CharSequence, buttonYes: CharSequence, buttonNo: CharSequence): AlertDialog {
        val startDialog = AlertDialog.Builder(act)
        startDialog.setTitle(title)
        startDialog.setMessage(message)
        startDialog.setPositiveButton(buttonYes) { dialogInterface, i ->
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 600)
                //return;
            } else {
                val startIntent = Intent(this, BarCodeReaderActivity::class.java)
                startActivity(startIntent)
                finish()
            }}
        startDialog.setNegativeButton(buttonNo) { dialogInterface, i -> finish()}
        startDialog.setCancelable(false)
        return startDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 600 && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val startIntent = Intent(this, BarCodeReaderActivity::class.java)
                startActivity(startIntent)
                finish()
            } else {
                Toast.makeText(this, "Вы запретили использовать камеру. Пройти регистрацию не получится", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
