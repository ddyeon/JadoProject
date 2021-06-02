package com.example.jadoproject

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.SyncStateContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import com.example.jadoproject.databinding.ActivityMainBinding
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val weeklyFragment = WeeklyFragment()
    private val groupFragment = GroupFragment()
    private val mypageFragment = MypageFragment()


    private val REQUEST_BLOOTH_ENABLE = 100
    private var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var mConnectedDeviceName: String? = null
    var isConnectionError = false
    var mConnectedTask: ConnectedTask? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.include.connectionBtn.setOnClickListener {
            connectionBluetooth()
        }

        val androidId : String = UUID.randomUUID().toString()
        Log.d("androidId", androidId)

        bottomnavi()
//076847
    }




    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun connectionBluetooth()
    {
        //bluetooth adapter
        val bleManager : BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bleManager.adapter
        if(mBluetoothAdapter == null)
        {
            Log.d("bluetooth", "This device is not implement Bluetooth")
            return
        }
        if(!mBluetoothAdapter.isEnabled)
        {
            val ble_enable_intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(ble_enable_intent, REQUEST_BLOOTH_ENABLE)
        }
        else
            run {
                Log.d("bluetooth", "Initialisaton successful")
                showPairedDevicesListDialog()
            }


    }

    private fun showPairedDevicesListDialog()
    {
        val devices : Set<BluetoothDevice> = mBluetoothAdapter.bondedDevices
        val pairedDevices = devices.toTypedArray()

        if(pairedDevices.isEmpty()) {
            Log.d("pairing", "No devices have been paired.\n you must pair it with another device.")
            return
        }

        val items : Array<String> = arrayOf("","","","","","","","","","","")
        for (i in pairedDevices.indices) {
            //items.add(pairedDevices[i].name)
            items[i] = pairedDevices[i].name
            Log.d("pairDevice", pairedDevices[i].name.toString())

        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select device")
        builder.setCancelable(false)
        builder.setItems(items) { dialog, which ->
            dialog?.dismiss()

            val task = ConnectTask(pairedDevices[which])
            task.execute()
        }
        builder.show()


    }

    @SuppressLint("StaticFieldLeak")
    inner class ConnectTask internal constructor(bluetoothDevice: BluetoothDevice) :
        AsyncTask<Void?, Void?, Boolean>() {
        private var mBluetoothSocket: BluetoothSocket? = null
        private var mBluetoothDevice: BluetoothDevice? = null

        override fun doInBackground(vararg params: Void?): Boolean {
            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery()
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket!!.connect()
            } catch (e: IOException) {
                // Close the socket
                try {
                    mBluetoothSocket!!.close()
                } catch (e2: IOException) {
                    Log.e(
                        "bluetooth", "unable to close() " +
                                " socket during connection failure", e2
                    )
                }
                return false
            }
            return true
        }

        override fun onPostExecute(isSucess: Boolean) {
            if (isSucess) {
                connected(mBluetoothSocket)
            } else {
                isConnectionError = true
                Log.d("connect:", "Unable to connect device")

            }
        }

        init {
            mBluetoothDevice = bluetoothDevice
            mConnectedDeviceName = bluetoothDevice.name

            //SPP
            val uuid: UUID = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")
            try {
                mBluetoothSocket = mBluetoothDevice!!.createRfcommSocketToServiceRecord(uuid)
                Log.d("uuid", "create socket for $mConnectedDeviceName")
            } catch (e: IOException) {
                Log.e("uuid", "socket create failed " + e.message)
            }

        }


    }

    fun connected(socket: BluetoothSocket?) {
        mConnectedTask = socket?.let { ConnectedTask(it) }
        mConnectedTask!!.execute()
    }

    inner class ConnectedTask(socket: BluetoothSocket) : AsyncTask<Void, String, Boolean>() {

        private var mInputStream: InputStream? = null
        private var mOutputStream: OutputStream? = null
        private var mBluetoothSocket: BluetoothSocket? = socket

        init {
            try {
                mInputStream = mBluetoothSocket!!.inputStream
                mOutputStream = mBluetoothSocket!!.outputStream
            } catch (e: IOException) {
                Log.e("socket", "socket not created", e)
            }
            Log.d("socket", "connected to $mConnectedDeviceName")
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            val readBuffer = ByteArray(1024)
            var readBufferPosition = 0
            while (true)
            {
                if(isCancelled)
                    return false
                try{
                    val bytesAvailable = mInputStream!!.available()
                    if (bytesAvailable > 0) {
                        val packetBytes = ByteArray(bytesAvailable)
                        mInputStream!!.read(packetBytes)
                        for (i in 0 until bytesAvailable) {
                            val b = packetBytes[i]
                            if (b == '\n'.toByte()) {
                                val encodedBytes = ByteArray(readBufferPosition)
                                System.arraycopy(
                                    readBuffer, 0, encodedBytes, 0,
                                    encodedBytes.size
                                )
                                /*val recvMessage = String(encodedBytes, "UTF-8")
                                readBufferPosition = 0
                                Log.d("message", "recv message: $recvMessage")
                                publishProgress(recvMessage)*/
                            } else {
                                readBuffer[readBufferPosition++] = b
                            }
                        }
                    }

                }
                catch (e : IOException)
                {
                    Log.e("disconnect", "disconnected", e);
                    return false;
                }
            }
        }


        override fun onPostExecute(isSucess: Boolean?) {
            super.onPostExecute(isSucess)
            if (!isSucess!!) {
                closeSocket()
                Log.d("loat", "Device connection was lost")
                isConnectionError = true
            }
        }

        override fun onCancelled(aBoolean: Boolean?) {
            super.onCancelled(aBoolean)
            closeSocket()
        }

        fun closeSocket() {
            try {
                mBluetoothSocket!!.close()
                Log.d("close socket", "close socket()")
            } catch (e2: IOException) {
                Log.e(
                    "not close", "unable to close() " +
                            " socket during connection failure", e2
                )
            }
        }

        fun write(msg: String) {
            var msg = msg
            msg += "\n"
            try {
                mOutputStream!!.write(msg.toByteArray())
                mOutputStream!!.flush()
            } catch (e: IOException) {
                Log.e("send error", "Exception during send", e)
            }

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_BLOOTH_ENABLE)
        {
            if(resultCode == RESULT_OK)
                showPairedDevicesListDialog()
            if(resultCode == RESULT_CANCELED)
                Log.d("fail", "you need to enable bluetooth")
        }
    }






    fun bottomnavi()
    {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(homeFragment)
                    }
                    R.id.week -> {
                        changeFragment(weeklyFragment)
                    }
                    R.id.group -> {
                        changeFragment(groupFragment)
                    }
                    R.id.mypage -> {
                        changeFragment(mypageFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.replace(R.id.fragmentContainerView2, fragment).commit()
    }




    }