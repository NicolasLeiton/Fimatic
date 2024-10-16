package com.utadeo.fimatic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import android.widget.ImageView
import kotlinx.coroutines.*

class Level1 : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private var playing = false
    private var animating = false
    private val path = listOf("Adelante", "Adelante")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = bundleOf(ARG_PARAM1 to "HOLA", ARG_PARAM2 to "1")
        supportFragmentManager.commit{
            setReorderingAllowed(true)
            add<Bloques>(R.id.Fragment_bloques, args = bundle)
        }

        val car: ImageView = findViewById(R.id.carImg)
        val piso_inicial:ImageView = findViewById(R.id.Piso_1)

        val claseCarro = Carro(car, this)


        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        // Observar los cambios en los datos
        viewModel.sharedData.observe(this) { data ->
            GlobalScope.launch {
                if (!playing){
                    playing = true; animating = true
                    animating = claseCarro.mover_carro(data)
                }
                else if(animating==false){
                    playing = claseCarro.reinicar(piso_inicial)
                }



            }
        }
    }

    fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    fun ir_atras(view: View){
        val home= Intent(this, Niveles::class.java)
        startActivity(home)
    }


}