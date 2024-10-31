package com.utadeo.fimatic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonSetiings: Button = findViewById(R.id.bot_config)
        val buttonExit: Button = findViewById(R.id.bot_salir)

        buttonSetiings.setOnClickListener {
            val intent = Intent(this, BTSettings::class.java)
            startActivity(intent)
        }
        buttonExit.setOnClickListener {
            finish()
        }
    }

    fun ir_niveles(view: View){
        val sig= Intent(this, Niveles::class.java)
        startActivity(sig)
    }


}