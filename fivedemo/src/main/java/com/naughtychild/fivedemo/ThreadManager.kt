package com.naughtychild.fivedemo

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.internal.SynchronizedObject

class ThreadManager(val bluetoothDevice: BluetoothDevice) {

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var connectThread: ConnectThread? = null
    var acceptThread: AcceptThread? = null
    var connectedThread: ConnectedThread? = null
    var socket: BluetoothSocket? = null

    fun tryConnectAsServer() {
        clearThread()
        acceptThread = AcceptThread(this)
        acceptThread?.start()
    }

    fun tryConnectAsClient() {
        clearThread()
        connectThread = ConnectThread(this, bluetoothDevice)
        connectThread!!.start()
    }

    fun connectedStream(socket: BluetoothSocket) {
        clearThread()
        connectedThread = ConnectedThread(socket, socket.remoteDevice)
        connectedThread?.start()
    }

    private fun clearThread() {
        acceptThread = null
        connectThread = null
        connectedThread = null
    }

    @Synchronized
    fun setIsClient(boo: Boolean) {
        isClient = boo
    }

    fun cancel() {
        socket?.close()
    }
}