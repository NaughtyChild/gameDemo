package com.naughtychild.fivedemo

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.InputStream
import java.io.OutputStream

//连接之后，进行管理 输入输出流线程
class ConnectedThread(val socket: BluetoothSocket, val bluetoothDevice: BluetoothDevice) : Thread() {
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    init {
        inputStream = socket.inputStream
        outputStream = socket.outputStream
    }

    override fun run() {
        super.run()
        //客户端接收消息，服务端发送消息
        if (isClient) {
            while (true) {
                val str = inputStream?.reader()?.readText()
                if (str != null) {
                    Log.d("ConnectedThread", "run: 收到消息，message：$str")
                }
            }
        } else {
            write("ssss")
        }
    }

    fun write(str: String) {
        outputStream?.writer()?.write(str)
    }
}