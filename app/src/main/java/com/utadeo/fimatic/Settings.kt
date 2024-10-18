package com.utadeo.fimatic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.UUID

//const val REQUEST_ENABLE_BT = 1
class Settings : AppCompatActivity() {
    /*
    //Bluetoothdapter
    lateinit var BtAdapter: BluetoothAdapter
    var AddressDevices: ArrayAdapter<String>? = null
    var NameDevices: ArrayAdapter<String>? = null
    companion object {
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var m_bluetoothSocket: BluetoothSocket? = null

        var isConnected: Boolean = false
        lateinit var address: String
    }

     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //AddressDevices = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        //NameDevices = ArrayAdapter( this, android.R.layout.simple_list_item_1)

    }
}