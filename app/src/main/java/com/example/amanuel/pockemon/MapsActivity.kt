package com.example.amanuel.pockemon

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        LoadPockemon()

        GetUserLocation()
     }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

var AccessForLoc=0

    fun checkPermision(){
        if(Build.VERSION.SDK_INT>23){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),AccessForLoc)
                return;
            }
        }
        GetUserLocation()
    }

    fun GetUserLocation(){
        var mylocate=MyLocationListener()
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,mylocate)
        var mythread=myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            AccessForLoc->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }
                else {
                    Toast.makeText(this,"We cant find ur adress enable Gps",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var Mylocation:Location?=null
//GET user location

    inner class MyLocationListener:LocationListener{


        constructor(){
            Mylocation=Location("Start")
            Mylocation!!.longitude=-122.3
            Mylocation!!.latitude=37.5
        }
        override fun onLocationChanged(location: Location?) {
           Mylocation=location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
    var oldLocation:Location?=null
    var playerPower=0.0
     inner class myThread:Thread{
    constructor():super(){
        oldLocation=Location("Start")
        oldLocation!!.longitude=0.0
        oldLocation!!.latitude=0.0
    }
        override fun run(){
            while(true){
                try{
                    if(oldLocation!!.distanceTo(Mylocation)==0f){continue}
                    oldLocation=Mylocation
                    runOnUiThread{
                        mMap!!.clear()

                        mMap!!.clear()

                        // show me
                        val sydney = LatLng(Mylocation!!.latitude, Mylocation!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet(" here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.soldier)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5f))

                        // show pockemons

                        for(i in 0..listPockemons.size-1){

                            var newPockemon=listPockemons[i]

                            if(newPockemon.IsCatch==false){

                                val pockemonLoc = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)
                                mMap!!.addMarker(MarkerOptions()
                                        .position(pockemonLoc)
                                        .title(newPockemon.name!!)
                                        .snippet(newPockemon.des!! +", power:"+ newPockemon!!.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))


                                if (Mylocation!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.IsCatch=true
                                    listPockemons[i]=newPockemon
                                    playerPower+=newPockemon.power!!
                                    Toast.makeText(applicationContext,
                                            "You catch new pockemon your new pwoer is " + playerPower,
                                            Toast.LENGTH_LONG).show()

                                }

                            }
                        }





                    }

                    Thread.sleep(1000)
                }catch (ex:Exception){}
            }
        }
    }
    var listPockemons=ArrayList<Pokemon>()
    fun LoadPockemon(){

        listPockemons.add(Pokemon(R.drawable.blue,
                "Charmander", "Charmander living in japan", 55.0, 37.7789994893035, -122.401846647263))
        listPockemons.add(Pokemon(R.drawable.bluegreen,
                "Bulbasaur", "Bulbasaur living in usa", 90.5, 37.7949568502667, -122.410494089127))
        listPockemons.add(Pokemon(R.drawable.yellowfire,
                "Squirtle", "Squirtle living in iraq", 33.5, 37.7816621152613, -122.41225361824))
    }
}
