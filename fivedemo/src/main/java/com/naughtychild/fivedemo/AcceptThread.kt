package com.naughtychild.fivedemo

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.lang.Exception

//服务线程，接收请求的线程
class AcceptThread(val threadManager: ThreadManager) : Thread() {
    var bluetoothSocket: BluetoothSocket? = null
    var serverSocket: BluetoothServerSocket? = null
    override fun run() {
        super.run()
        try {
            serverSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("name", MY_UUID)
        } catch (e: Exception) {
            Log.d("MainActivity", "run: server 发生异常，${e.message}，作为客户端启动")
            serverSocket?.close()
            threadManager.tryConnectAsClient()
            return
        }
        try {
            bluetoothSocket = serverSocket?.accept()
        } catch (e: Exception) {
            bluetoothSocket?.close()
            Log.d("MainActivity", "run: server 发生异常，${e.message}，作为客户端启动")
            threadManager.tryConnectAsClient()
            return
        }

        Log.d("AcceptThread", "run: 作为服务端启动成功，isClient=$isClient")
        threadManager.connectedStream(bluetoothSocket!!)
    }
}