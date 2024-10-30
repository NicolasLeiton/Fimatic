package com.utadeo.fimatic

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID


object BluetoothController {
    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private const val UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB" // UUID estándar para dispositivos Bluetooth SPP

    // Conectar a un dispositivo Bluetooth
    @SuppressLint("MissingPermission")
    fun connect(deviceAddress: String): Boolean {
        val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        return try {
            val uuid = UUID.fromString(UUID_STRING)
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    // Enviar datos a través del socket Bluetooth
    fun sendData(data: String): Boolean {
        return try {
            bluetoothSocket?.outputStream?.write(data.toByteArray())
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    // Desconectar el dispositivo Bluetooth
    fun disconnect() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        bluetoothSocket = null
    }

    // Verificar si el Bluetooth está conectado
    fun isConnected(): Boolean {
        return bluetoothSocket?.isConnected == true
    }
}
