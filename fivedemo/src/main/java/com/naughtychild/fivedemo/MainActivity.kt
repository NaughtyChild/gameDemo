package com.naughtychild.fivedemo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val scanDevicesAdapter by lazy { ArrayAdapter<String>(this@MainActivity, R.layout.list_item) }
    val pairDevicesAdapter by lazy {
        ArrayAdapter<String>(this@MainActivity, R.layout.list_item)
    }
    lateinit var receiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanBt.setOnClickListener {
            if (bluetoothAdapter.isEnabled) {
                startScanBlueTooth()
            } else {
                Snackbar.make(
                    scanBt, R.string.open_bluetooth,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        pairDevicesLV.adapter = pairDevicesAdapter
        scanDevicesLV.adapter = scanDevicesAdapter
        pairDevicesLV.setOnItemClickListener { parent, view, position, id ->

        }
        val devices = bluetoothAdapter.bondedDevices
        devices.forEach {
            pairDevicesAdapter.add(it.name)
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 2
        )
    }

    private fun startScanBlueTooth() {
        Log.d("MainActivity", "startScanBlueTooth: ")
        registerBroadcast()
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        } else {
            bluetoothAdapter.startDiscovery()
        }
    }

    private fun registerBroadcast() {
        Log.d("MainActivity", "registerBroadcast: ")
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                Log.d("MainActivity", "onReceive: dddd ")
                val action = intent.action ?: return
                when (action) {
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        scanMessageTv.setText("扫描中。。。。。")
                    }
                    BluetoothDevice.ACTION_FOUND -> {
                        // 从Intent中获取BluetoothDevice对象
                        val device = intent
                            .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        val name = device.name ?: return
                        Toast.makeText(this@MainActivity, "发现：${device.name}", Toast.LENGTH_SHORT).show()
                        scanDevicesAdapter.add(device.name)
                    }
                    BluetoothDevice.ACTION_ACL_CONNECTED -> {

                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        scanMessageTv.setText("扫描结束")
                        Toast.makeText(this@MainActivity, "扫描完毕", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        }
        this.registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(receiver)
    }
}
