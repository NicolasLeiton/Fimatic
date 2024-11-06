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
        val location = IntArray(2)
        inico.getLocationOnScreen(location)
        withContext(Dispatchers.Main) {
            // Mover el carro a la casilla original
            carImg.x = location[0].toFloat() + 9.dpToPx()
            carImg.y = location[1].toFloat() + 8.dpToPx()
            //Rotacion por defecto
            carImg.rotation = 0f

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
        var salida_str = ""
        for((i, paso) in list_inst.withIndex()){

            //Movimiento
            if(paso=="Adelante"){
                mover_adelante()
            }
            else if (paso=="Derecha"){
                mover_derecha()
            }
            else if (paso=="Izquierda"){
                mover_izquierda()
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
            salida_str = "Vas bien... pero aún te falta un poco"
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
        if (BluetoothController.isConnected()){
            //Si puede le envia la orden al carro
            BluetoothController.sendData("A")
        }
        val direction = (carImg.rotation % 360 + 360) % 360 // Aseguramos que este de 0 a 360
        Log.i("Carro", "Esta en $direction grados")

        //Dependiendo la orintacion del carro la direccion de la animacion cambia
        val animator:ObjectAnimator = when (direction) {
            0f -> ObjectAnimator.ofFloat(carImg, "translationY", carImg.translationY, carImg.translationY - 90.dpToPx())
            90f -> ObjectAnimator.ofFloat(carImg, "translationX", carImg.translationX, carImg.translationX + 90.dpToPx())
            180f -> ObjectAnimator.ofFloat(carImg, "translationY", carImg.translationY, carImg.translationY + 90.dpToPx())
            else -> ObjectAnimator.ofFloat(carImg, "translationX", carImg.translationX, carImg.translationX - 90.dpToPx())
        }

        withContext(Dispatchers.Main){
            animator.duration = 1200
            animator.start()

            delay(1400)
        }

    }

    private suspend fun mover_derecha(){
        if (BluetoothController.isConnected()){
            //Si puede le envia la orden al carro
            BluetoothController.sendData("R")
        }

        withContext(Dispatchers.Main){

            val animator = ObjectAnimator.ofFloat(carImg, "rotation", carImg.rotation + 90f)
            animator.duration = 900
            animator.start() // Iniciar la animación

            delay(1100)
        }
    }

    private suspend fun mover_izquierda(){
        if (BluetoothController.isConnected()){
            //Si puede le envia la orden al carro
            BluetoothController.sendData("L")
        }

        withContext(Dispatchers.Main){

            val animator = ObjectAnimator.ofFloat(carImg, "rotation", carImg.rotation - 90f)
            animator.duration = 850
            animator.start() // Iniciar la animación

            delay(1050)
        }
    }

    fun Int.dpToPx(): Float {
        return (this * context.resources.displayMetrics.density)

    }
}