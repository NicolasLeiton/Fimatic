package com.utadeo.fimatic

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Carro(private val carImg: ImageView, private val context: Context, private val salida: TextView, private val trofeo: ImageView) {

    suspend fun reinicar(inico: ImageView): Boolean {
        var location = IntArray(2)
        inico.getLocationOnScreen(location)
        location[1]+=9.dpToPx()//Ajustar al centro
        location[0]+=8.dpToPx()
        withContext(Dispatchers.Main) {
            // Mover el carro a la casilla original
            carImg.x = location[0].toFloat()
            carImg.y = location[1].toFloat()

            salida.text = ""
            trofeo.visibility = View.INVISIBLE
        }

        if (BluetoothController.isConnected()){
            BluetoothController.sendData("S")
        }

        return false
    }

    suspend fun mover_carro(instrucciones:String, camino:List<String>): Boolean {
        val list_inst = instrucciones.split(" ") //Convertir el string en una lista
        var salida_str:String =""
        for((i, paso) in list_inst.withIndex()){

            //Movimiento
            if(paso=="Adelante"){
                if (BluetoothController.isConnected()){
                    BluetoothController.sendData("A")
                }
                mover_adelante()
            }
            else if (paso=="Derecha"){
                if (BluetoothController.isConnected()){
                    BluetoothController.sendData("R")
                }
            }

            //Verificacion
            if(i+1>camino.size) {
                salida_str = "Superaste el límite de pasos :("
                break
            }
            if (paso==camino[i]){
                continue
            }
            else{
                salida_str = "Nop, te equivocaste en tu bloque número ${i+1} :("
                break
            }

        }
        if (list_inst.size<camino.size){
            salida_str = "Casi.. te falta poco"
        }
        else if(salida_str==""){
            salida_str = "¡¡Muy bien, lo conseguiste!!"
            withContext(Dispatchers.Main) {
                trofeo.visibility = View.VISIBLE
            }
            if (BluetoothController.isConnected()){
                BluetoothController.sendData("S")
                BluetoothController.sendData("C")
            }
        }
        withContext(Dispatchers.Main) {
            salida.text = salida_str
            if (salida_str!= "¡¡Muy bien, lo conseguiste!!" && BluetoothController.isConnected()){
                BluetoothController.sendData("S")
                BluetoothController.sendData("E")
            }
        }
        return false
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