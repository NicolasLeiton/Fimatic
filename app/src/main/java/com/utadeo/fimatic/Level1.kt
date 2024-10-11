package com.utadeo.fimatic

import android.animation.ObjectAnimator
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

        val carro: ImageView = findViewById(R.id.carImg)
        val piso_inicial:ImageView = findViewById(R.id.Piso_1)

        val claseCarro: Carro = Carro(carro, this)


        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        // Observar los cambios en los datos
        viewModel.sharedData.observe(this) { data ->
            GlobalScope.launch {
                if (!playing){
                    playing = true
                    mover_carro(data, carro)
                }
                else if(animating==false){
                    reinicar(carro, piso_inicial)
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

    fun reinicar(carro:ImageView, inico:ImageView){
        val location = IntArray(2)
        inico.getLocationOnScreen(location)
        location[1]+=9.dpToPx()
        location[0]+=8.dpToPx()

        // Mover el carro a la casilla original
        carro.x = location[0].toFloat()
        carro.y = location[1].toFloat()
        playing = false
    }

    suspend fun mover_carro(instrucciones:String, carro:ImageView){
        animating = true
        val list_inst = instrucciones.split(" ")

        for((i, paso) in list_inst.withIndex()){
            Log.d("Level1", "Paso numero ${i}: ${paso}")
            if(paso=="Adelante"){
                mover_adelante(carro)

            }
        }
        animating=false

    }
    private suspend fun mover_adelante(carro:ImageView){
        withContext(Dispatchers.Main){

            val animator = ObjectAnimator.ofFloat(carro, "translationY", carro.translationY, carro.translationY - 90.dpToPx())
            animator.duration = 1000 // Establecer la duración de la animación
            animator.start()

            delay(1000)
        }
    }




}