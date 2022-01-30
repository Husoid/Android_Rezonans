package com.rezonans

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rezonans.Logon.LogonActivity
import android.os.Handler


class HeadpieceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headpiece)

        val handler = Handler()
        handler.postDelayed(
            Runnable { Mainstart(View(this)) },
            3000
        )
    }

    fun Mainstart (view: View) {
        val intent = Intent(this, LogonActivity::class.java)
        startActivity(intent)
        finish()
    }
}
