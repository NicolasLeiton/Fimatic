package com.utadeo.fimatic

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class Level2 : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private var playing = false
    private var animating = false
    private val path = listOf("Adelante", "Adelante", "Derecha", "Adelante")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = bundleOf(ARG_PARAM1 to "Level2", ARG_PARAM2 to 2)
        supportFragmentManager.commit{
            setReorderingAllowed(true)
            add<Bloques>(R.id.Fragment_bloques, args = bundle)
        }

        val car: ImageView = findViewById(R.id.carImg)
        val piso_inicial:ImageView = findViewById(R.id.Piso_1)
        val text_out:TextView = findViewById(R.id.text_result)
        val trophy:ImageView = findViewById(R.id.trophy_img)
        val backButton: ImageButton = findViewById(R.id.imageButton2)

        backButton.setOnClickListener {
            if (text_out.text == "¡¡Muy bien, lo conseguiste!!"){
                lifecycleScope.launch(Dispatchers.IO) {
                    SaveValues()
                }
            }
            finish()
        }

        val claseCarro = Carro(car, this, text_out, trophy)


        if (BluetoothController.isConnected()){
            Log.i("Level2", "EL BT ESTA CONECTADO")
        }
        else{
            Log.i("Level2", "EL BT NO ESTA CONECTADO")
        }


        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        // Observar los cambios en los datos
        viewModel.sharedData.observe(this) { data ->
            GlobalScope.launch {
                if (!playing){
                    playing = true; animating = true
                    animating = claseCarro.mover_carro(data, path)
                }
                else if(animating==false){
                    playing = claseCarro.reinicar(piso_inicial)

                }
            }



        }
    }


    private suspend fun SaveValues(){
        dataStore.edit{pref ->
            pref[booleanPreferencesKey("Level2")] = true
        }
    }



}