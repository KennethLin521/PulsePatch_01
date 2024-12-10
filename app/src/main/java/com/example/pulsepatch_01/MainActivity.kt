package com.example.pulsepatch_01

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.bluetooth.le.BluetoothLeScanner

import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.pulsepatch_01.databinding.ActivityMainBinding
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    // Check to see if the Bluetooth classic feature is available.
    val bluetoothAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)

    // Check to see if the BLE feature is available.
    val bluetoothLEAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    private var scanning = false
    //private val handler = Handler()
    private val SCAN_PERIOD: Long = 10000 // Set scan duration to 10 seconds
/*
    private val bluetoothLeScanner by lazy { bluetoothAdapter?.bluetoothLeScanner }

*/

    companion object {
        const val REQUEST_ENABLE_BT = 1
    }

    private fun initializeBluetooth() {
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            startBleScan(bluetoothAdapter)
        }
    }


    private fun startBleScan(bluetoothAdapter: BluetoothAdapter) {
        val scanner = bluetoothAdapter.bluetoothLeScanner

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        var bluetoothAdapter = bluetoothManager?.adapter

        // Check if Bluetooth is supported
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show()
        } else {
            binding.setOnClickListener { view ->
                Snackbar.make(view, "Starting BLE Scan", Snackbar.LENGTH_LONG).show()
                //scanLeDevice()
            }
        }
        /**
        final BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = btManager.getAdapter();

        checkBt();
        */
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled, proceed with scan
                initializeBluetooth()
            } else {
                // User denied enabling Bluetooth
                Toast.makeText(this, "Bluetooth must be enabled to scan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        // Here you can access the BLE device details, like result.device.name
        val deviceName = result.device.name ?: "Unknown Device"
        val deviceAddress = result.device.address
        Toast.makeText(this@MainActivity, "Found device: $deviceName, $deviceAddress", Toast.LENGTH_SHORT).show()
        // Optionally, add devices to a list for display or stop scanning if this is the desired device.
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        super.onBatchScanResults(results)
        // Handle multiple results if using batch scanning
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Toast.makeText(this@MainActivity, "Scan failed with error: $errorCode", Toast.LENGTH_SHORT).show()
    }
}

    private fun startScan() {
        if (bluetoothAdapter?.isEnabled == true) {
            val scanSettings = ScanSettings.Builder().build()
            //bluetoothLeScanner?.startScan(null, scanSettings, scanCallback)
            //bluetoothLeScanner?.startScan(leScanCallback)
        }
    }
    private fun stopScan() {
        //bluetoothLeScanner?.stopScan(scanCallback)
    }



}

private fun ActivityMainBinding.setOnClickListener(function: Any) {}


