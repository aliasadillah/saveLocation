package com.example.savelocation
import android.Manifest
import DataBaseHandler
import User
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import app.com.savelocation.R
import app.com.savelocation.saved
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.String as String1

class MainActivity : AppCompatActivity() {
   private var fusedLocationClient: FusedLocationProviderClient? = null
   private var lastLocation: Location? = null
//   private var latitudeText: TextView? = null
//   private var longitudeText: TextView? = null
//   private var latitudeLabel: String? = null
//   private var longitudeLabel:String? = null
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      if (!checkPermissions()) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions()
         }
      }


      setContentView(R.layout.activity_main)
//      latitudeText = findViewById<String>(R.id.latitudeText)
//      longitudeText = findViewById<String>(R.id.longitudeText)
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
      title = "KotlinApp"
      val context = this
      val db = DataBaseHandler(context)
      btnInsert.setOnClickListener {
         getLastLocation()
         if (editTextName.text.toString().isNotEmpty()) {

//            if (task.isSuccessful && task.result != null) {
//               lastLocation = task.result
//               latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
//               longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
//            }
//            else {
//               Log.w(TAG, "getLastLocation:exception", task.exception)
//               showMessage("No location detected. Make sure location is enabled on the device.")
//            }
            val user = User(editTextName.text.toString(), (lastLocation)!!.longitude.toFloat(), (lastLocation)!!.latitude.toFloat())
            db.insertData(user)
            clearField()
         }
         else {
            Toast.makeText(context, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
         }
      }
      btnRead.setOnClickListener {
         val intent = Intent(this, saved::class.java)
         startActivity(intent)
//         val data = db.readData()
//         tvResult.text = ""
//         for (i in 0 until data.size) {
//            tvResult.append(
//               data[i].id.toString() + " " + data[i].name + " " + data[i].longitude +"/n"+ data[i].latitude +"/n"+"/n"
//            )
//         }
      }
   }
   private fun clearField() {
      editTextName.text.clear()
   }
//   public override fun onStart() {
//      super.onStart()
//
//   }
   private fun getLastLocation() {
      if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
         ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
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
      fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
         if (task.isSuccessful && task.result != null) {
            lastLocation = task.result
//            latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
//            longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
         }
         else {
            Log.w(TAG, "getLastLocation:exception", task.exception)
//            showMessage("No location detected. Make sure location is enabled on the device.")
         }
      }
   }
//   private fun showMessage(string: String1) {
//      val container = findViewById<View>(R.id.linearLayout)
//      if (container != null) {
//         Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()
//      }
//   }
   private fun showSnackbar(
           mainTextStringId: String1, actionStringId: String1,
           listener: View.OnClickListener
   ) {
      Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
   }
   private fun checkPermissions(): Boolean {
      val permissionState = ActivityCompat.checkSelfPermission(
      this,
      Manifest.permission.ACCESS_COARSE_LOCATION
   )
   return permissionState == PackageManager.PERMISSION_GRANTED
}

private fun startLocationPermissionRequest() {
   ActivityCompat.requestPermissions(
      this@MainActivity,
      arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
      REQUEST_PERMISSIONS_REQUEST_CODE
   )
}
private fun requestPermissions() {
   val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
      this,
      Manifest.permission.ACCESS_COARSE_LOCATION
   )
   if (shouldProvideRationale) {
      Log.i(TAG, "Displaying permission rationale to provide additional context.")
      showSnackbar("Location permission is needed for core functionality", "Okay",
      View.OnClickListener {
         startLocationPermissionRequest()
      })
   }
   else {
      Log.i(TAG, "Requesting permission")
      startLocationPermissionRequest()
   }
}
override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String1>,
        grantResults: IntArray
) {
   Log.i(TAG, "onRequestPermissionResult")
   if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      when {
         grantResults.isEmpty() -> {
            // If user interaction was interrupted, the permission request is cancelled and you
            // receive empty arrays.
            Log.i(TAG, "User interaction was cancelled.")
         }
         grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
            // Permission granted.
            getLastLocation()
         }
         else -> {
            showSnackbar("Permission was denied", "Settings",
               View.OnClickListener {
                  // Build intent that displays the App settings screen.
                  val intent = Intent()
                  intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                  val uri = Uri.fromParts(
                     "package",
                     Build.DISPLAY, null
                  )
                  intent.data = uri
                  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                  startActivity(intent)
                  }
               )
            }
         }
      }
   }
   companion object {
      private val TAG = "LocationProvider"
      private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
   }
}