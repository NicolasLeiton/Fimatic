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

class Level1 : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = bundleOf(ARG_PARAM1 to "HOLA", ARG_PARAM2 to "2")
        supportFragmentManager.commit{
            setReorderingAllowed(true)
            add<Bloques>(R.id.Fragment_bloques, args = bundle)
        }


        // Obtener el ViewModel compartido
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        // Observar los cambios en los datos
        viewModel.sharedData.observe(this) { data ->
            // Hacer algo con los datos
            mover_carro(data)
        }
    }


    fun ir_home(view: View){
        val home= Intent(this, MainActivity::class.java)
        startActivity(home)
    }

    fun mover_carro(instrucciones:String){
        val list_inst = instrucciones.split(" ")

        for((i, paso) in list_inst.withIndex()){
            Log.d("Level1", "Paso numero ${i}: ${paso}")
        }

    }


}