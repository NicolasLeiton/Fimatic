package com.utadeo.fimatic

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore by preferencesDataStore(name = "SAVES")
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

        val button_lv1: Button = findViewById(R.id.bot_lv1)
        val button_lv2: Button = findViewById(R.id.bot_lv2)
        val button_lv3: Button = findViewById(R.id.bot_lv3)

        lifecycleScope.launch(Dispatchers.IO) {
            SavedValues().collect{
                withContext(Dispatchers.Main){
                    if (it[0]){
                        button_lv1.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    }
                    if (it[1]){
                        button_lv2.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    }
                    if (it[2]){
                        button_lv3.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    }
                }

            }


        }


    }

    private fun SavedValues() = dataStore.data.map { pref ->
            listOf(
                pref[booleanPreferencesKey("Level1")] ?: false,
                pref[booleanPreferencesKey("Level2")] ?: false,
                pref[booleanPreferencesKey("Level3")] ?: false
            )
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