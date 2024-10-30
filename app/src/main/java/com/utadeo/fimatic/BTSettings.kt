package com.utadeo.fimatic

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.util.UUID

const val REQUEST_ENABLE_BT = 1

class BTSettings : AppCompatActivity() {

    //Bluetoothdapter
    lateinit var BtAdapter: BluetoothAdapter
    var AddressDevices: ArrayAdapter<String>? = null
    var NameDevices: ArrayAdapter<String>? = null
    private val REQUEST_BLUETOOTH_PERMISSION = 1


    companion object {
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var bluetoothSocket: BluetoothSocket? = null

        var isConnected: Boolean = false
        lateinit var address: String
    }






    //private lateinit var bluetoothAdapter: BluetoothAdapter


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_btsettings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        AddressDevices = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        NameDevices = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        val turnBT: Switch = findViewById(R.id.Blout_switch)
        val devices_BT: Button = findViewById(R.id.devices_buttom)
        val spinnerDevices: Spinner = findViewById(R.id.Devices_spinner)
        val connect_BT: Button = findViewById(R.id.connect_bt)

        val someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == REQUEST_ENABLE_BT) {
                Log.i("Settings","ACTIVIDAD REGISTRADA")
            }
        }

        //Inicializacion del bluetooth adapter
        BtAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        //Checar si esta encendido o apagado
        if (BtAdapter == null) {
            Toast.makeText( this, "Bluetooth no está disponible en este dipositivo", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Bluetooth está disponible en este dispositivo", Toast.LENGTH_LONG)
                .show()
        }

        turnBT.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (checkAndRequestBluetoothPermission()){
                    //Mostrar opciones
                    devices_BT.visibility = View.VISIBLE
                    spinnerDevices.visibility = View.VISIBLE
                    connect_BT.visibility = View.VISIBLE

                    if (BtAdapter.isEnabled) {
                        //si ya está activado
                        Toast.makeText(this, "Bluetooth ya se encuentra activado", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        //Encender Bluetooth
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Log.i("Settings", "ActivityCompat#requestPermissions")
                        }
                        someActivityResultLauncher.launch(enableBtIntent)
                    }
                }
                else{
                    Toast.makeText(this, "No se han concedido permisos para usar esta funcion", Toast.LENGTH_LONG)
                        .show()
                }



            }

            else {
                // Ocultar opciones
                devices_BT.visibility = View.INVISIBLE
                spinnerDevices.visibility = View.INVISIBLE
                connect_BT.visibility = View.INVISIBLE

                if (!BtAdapter.isEnabled) {
                //Si ya está desactivado
                    Toast.makeText( this, "Bluetooth ya se encuentra desactivado", Toast.LENGTH_LONG).show()
                } else {
                    //Encender Bluetooth
                    BtAdapter.disable()
                    Toast.makeText(this, "Se ha desactivado el bluetooth", Toast.LENGTH_LONG).show()
                }
            }
        }

        devices_BT.setOnClickListener {
            if (BtAdapter.isEnabled) {
                val pairedDevices: Set<BluetoothDevice>? = BtAdapter?.bondedDevices
                AddressDevices!!.clear()
                NameDevices!!.clear()

                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    AddressDevices!!.add(deviceHardwareAddress)
                    NameDevices!!.add(deviceName)
                }

                //ACTUALIZO LOS DISPOSITIVOS
                spinnerDevices.setAdapter(NameDevices)
            } else {
                val noDevices = "Ningun dispositivo pudo ser emparejado"
                AddressDevices!!.add(noDevices)
                NameDevices!!.add(noDevices)
                Toast.makeText(this, "Primero vincule el dispositivo bluetooth", Toast.LENGTH_LONG)
                    .show()
            }
        }

        connect_BT.setOnClickListener {
            try {
                if (bluetoothSocket == null || !isConnected) {
                    val IntValSpin = spinnerDevices.selectedItemPosition
                    address = AddressDevices!!.getItem(IntValSpin).toString()

                    // Cancel discovery because it otherwise slows down the connection.
                    BtAdapter?.cancelDiscovery()
                    val device: BluetoothDevice = BtAdapter.getRemoteDevice(address)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                    bluetoothSocket!!.connect()
                }

                Toast.makeText(this, "CONEXION EXITOSA", Toast.LENGTH_LONG).show()
                Log.i("Settings", "CONEXION EXITOSA")
            } catch (e: IOException) {
                //connectSuccess = false
                e.printStackTrace()
                Toast.makeText(this, "ERROR DE CONEXION", Toast.LENGTH_LONG).show()
                Log.i("Settings", "ERROR DE CONEXION")
            }
        }

    }

    private fun checkBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12 y superiores, verifica el permiso BLUETOOTH_CONNECT
            ActivityCompat.checkSelfPermission(
                this,
                "android.permission.BLUETOOTH_CONNECT"
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // No es necesario solicitar este permiso en versiones anteriores a Android 12
            true
        }
    }

    private fun requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Solicitar el permiso BLUETOOTH_CONNECT en Android 12 y superiores
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.BLUETOOTH_CONNECT"),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, ahora puedes acceder a funcionalidades de Bluetooth
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado, maneja esta situación según sea necesario
                Toast.makeText(this, "Permiso de Bluetooth denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Llama a esta función antes de usar funcionalidades de Bluetooth
    private fun checkAndRequestBluetoothPermission(): Boolean {
        if (!checkBluetoothPermission()) {
            requestBluetoothPermission()
            return checkBluetoothPermission()
        } else {
           return true
        }

    }

}