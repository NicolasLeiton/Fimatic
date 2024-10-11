package com.utadeo.fimatic

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Carro(private val carImg: ImageView, private val context: Context) {

    private var playing = false
    private var animating = false

    fun reinicar(inico: ImageView){
        val location = IntArray(2)
        inico.getLocationOnScreen(location)
        location[1]+=9.dpToPx()
        location[0]+=8.dpToPx()

        // Mover el carro a la casilla original
        carImg.x = location[0].toFloat()
        carImg.y = location[1].toFloat()
        playing = false
    }

    suspend fun mover_carro(instrucciones:String){
        animating = true
        val list_inst = instrucciones.split(" ")

        for((i, paso) in list_inst.withIndex()){
            Log.d("Level1", "Paso numero ${i}: ${paso}")
            if(paso=="Adelante"){
                mover_adelante()

            }
        }
        animating=false

    }
    private suspend fun mover_adelante(){
        withContext(Dispatchers.Main){

            val animator = ObjectAnimator.ofFloat(carImg, "translationY", carImg.translationY, carImg.translationY - 90.dpToPx())
            animator.duration = 1000 // Establecer la duración de la animación
            animator.start()

            delay(1000)
        }
    }
    fun Int.dpToPx(): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}