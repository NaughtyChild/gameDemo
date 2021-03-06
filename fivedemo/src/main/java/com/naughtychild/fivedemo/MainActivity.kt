package com.naughtychild.fivedemo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
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
import com.naughtychild.fivedemo.ext.click
import com.naughtychild.fivedemo.ext.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {
    private var outS: OutputStream? = null
    private var input: InputStream? = null
    private var socket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val scanDevicesAdapter by lazy { ArrayAdapter<String>(this@MainActivity, R.layout.list_item) }
    val pairDevicesAdapter by lazy {
        ArrayAdapter<String>(this@MainActivity, R.layout.list_item)
    }
    var serverThread: Thread? = null
    var clientThread: Thread? = null
    lateinit var receiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanBt.setOnClickListener {
            if (bluetoothAdapter.isEnabled) {
                startScanBlueTooth()
            } else {
                "开启蓝牙".toast(this)
            }
        }
        pairDevicesLV.adapter = pairDevicesAdapter
        scanDevicesLV.adapter = scanDevicesAdapter
        pairDevicesLV.setOnItemClickListener { parent, view, position, id ->
            val itemInfo = pairDevicesAdapter.getItem(position) ?: return@setOnItemClickListener
            val address = itemInfo.split("%%")[1]
            val device = bluetoothAdapter.getRemoteDevice(address)
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            clientStart(device)
        }
        serverBt.click {
            serverStart()
        }
        clientBt.click {

        }
        scanDevicesLV.setOnItemClickListener { parent, view, position, id ->
            val content = scanDevicesAdapter.getItem(position) ?: return@setOnItemClickListener
            val address = content.split("%%")[1]
            val device = bluetoothAdapter.getRemoteDevice(address)
            connectDevice(device)
        }

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 2
        )
        registerBroadcast()
    }

    private fun clientStart(device: BluetoothDevice) {
        Log.d("MainActivity", "clientStart: ")
        GlobalScope.launch(Dispatchers.IO) {
            socket = getClientSocket(device)
            if (clientConnect(socket)) {
                chatWithServer(socket!!)
            }
        }
//        clientThread = object : Thread() {
//            override fun run() {
//                super.run()
//                try {
//                    socket = device.createRfcommSocketToServiceRecord(MY_UUID)
//                    socket?.connect()
//                } catch (e: Exception) {
//                    Log.d("MainActivity", "run: 客户端连接失败")
//                    return
//                }
//                handleClient(socket)
//            }
//        }
//        clientThread?.start()
    }

    private suspend fun chatWithServer(socket: BluetoothSocket) {
        outS = socket.outputStream
        for (i in 1 until 5) {
            outS?.write("今天天气很不错".toByteArray())
        }
        Log.d("MainActivity", "chatWithServer: 客户端写入成功")
    }

    private suspend fun clientConnect(socket: BluetoothSocket?): Boolean {
        socket?.connect()
        return true
    }

    private suspend fun getClientSocket(device: BluetoothDevice): BluetoothSocket? {
        socket = device.createRfcommSocketToServiceRecord(MY_UUID)
        return socket
    }

    private fun handleClient(socket: BluetoothSocket?) {
        try {
            outS = socket!!.outputStream
            for (i in 0..5) {
                Thread.sleep(1000)
                outS!!.write("今天太阳不错呀".toByteArray())
            }
            Log.d("MainActivity", "run: 客户端写入完成")
        } catch (e: Exception) {
            Log.d("MainActivity", "run:客户端写入失败 ${e.message}")
        }
    }

    private fun serverStart() {
        Log.d("MainActivity", "serverStart: ")
        GlobalScope.launch(Dispatchers.IO) {
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("name", MY_UUID)
            } catch (e: Exception) {
                Log.d("MainActivity", "serverStart: 服务端启动 失败")
                return@launch
            }
            try {
                Log.d("MainActivity", "run: 服务端在监听。。")
                socket = serverSocket?.accept()
                Log.d("MainActivity", "run: 服务端监听到")
            } catch (e: Exception) {
                Log.d("MainActivity", "serverStart: 服务端连接失败 failed")
                return@launch
            }
            serverSocket?.close()
            try {
                input = socket!!.inputStream
                val byteArray = ByteArray(1024)
                while (true) {
                    val length = input!!.read(byteArray)
                    // 创建接收的信息的字符串
                    val readMessage = String(byteArray, 0, length ?: 0) //用系统默认的字符集把字节数组的一个序列转换为字符串
                    Log.d("MainActivity", "run:服务端接收完成:$readMessage)}")
                }
            } catch (e: Exception) {
                Log.d("MainActivity", "run: 服务端接收失败11${e.message}")
            }
        }
    }

    private fun connectDevice(device: BluetoothDevice) {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        device.createBond()
    }

    private fun startScanBlueTooth() {
        pairDevicesAdapter.clear()
        scanDevicesAdapter.clear()
        Log.d("MainActivity", "startScanBlueTooth: ")
        val devices = bluetoothAdapter.bondedDevices
        devices.forEach {
            pairDevicesAdapter.add("${it.name}%%${it.address}")
        }
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
                        scanDevicesAdapter.add("${device.name}%%${device.address}")
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        scanMessageTv.setText("扫描结束")
                        Toast.makeText(this@MainActivity, "扫描完毕", Toast.LENGTH_SHORT).show()
                    }
                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        Log.d("MainActivity", "onReceive: ACTION_ACL_CONNECTED")
                        val device = intent
                            .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                        tryChat(device)
                        Log.d("MainActivity", "onReceive: ${device.name}")
                    }
                    BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                        val device = intent
                            .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        if (device.bondState != BluetoothDevice.BOND_BONDED) {
                            return
                        }
                        Log.d(
                            "MainActivity",
                            "onReceive: ${device.name}"
                        )
                        val itemInfo = "${device.name}%%${device.address}"
                        val position = scanDevicesAdapter.getPosition(itemInfo)
                        if (position > 0) {
                            scanDevicesAdapter.remove(itemInfo)
                        }
                        if (pairDevicesAdapter.getPosition(itemInfo) < 0) {
                            pairDevicesAdapter.add(itemInfo)
                        }
                    }
                }
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        this.registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(receiver)

    }
}
