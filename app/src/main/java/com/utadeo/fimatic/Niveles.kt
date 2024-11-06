package com.utadeo.fimatic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Niveles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_niveles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun ir_home(view: View){
        finish()
    }
    fun ir_abajo(view: View){
        val home= Intent(this, Niveles2::class.java)
        startActivity(home)
    }
    fun ir_nivel1(view: View){
        val home= Intent(this, Level1::class.java)
        startActivity(home)
    }
    fun ir_nivel2(view: View){
        val home= Intent(this, Level2::class.java)
        startActivity(home)
    }
    fun ir_nivel3(view: View){
        val home= Intent(this, Level3::class.java)
        startActivity(home)
    }
}