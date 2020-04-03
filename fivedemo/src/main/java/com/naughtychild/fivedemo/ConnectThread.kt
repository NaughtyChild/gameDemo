package com.naughtychild.fivedemo

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.lang.Exception

//客户端线程，连接蓝牙服务
class ConnectThread(
    val manager: ThreadManager,
    val bluetoothDevice: BluetoothDevice
) : Thread() {
    var bluetoothSocket: BluetoothSocket? = null

    override fun run() {
        super.run()
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID)
        } catch (e: Exception) {
            bluetoothSocket?.close()
            Log.d("ConnectThread", "run: 作为客户端启动失败，作为服务端开始启动")
            manager.tryConnectAsServer()
            return
        }
        manager.setIsClient(true)
        Log.d("ConnectThread", "run: 作为客户端启动成功，isClient=$isClient")
        manager.connectedStream(socket = bluetoothSocket!!)
    }
}