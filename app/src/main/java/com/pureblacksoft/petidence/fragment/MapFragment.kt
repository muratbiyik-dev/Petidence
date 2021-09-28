package com.pureblacksoft.petidence.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.databinding.FragmentMapBinding
import com.pureblacksoft.petidence.parser.JsonParser
import com.skyfishjy.library.RippleBackground
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

@SuppressLint("MissingPermission")
class MapFragment: Fragment(R.layout.fragment_map), OnMapReadyCallback
{
    companion object
    {
        private const val REQUEST_CODE = 111
        private const val DEFAULT_ZOOM = 15f

        private lateinit var mGoogleMap: GoogleMap
        private lateinit var mRippleMF: RippleBackground
    }

    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootMF: View

    private lateinit var mFusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var mLastLocation: Location
    private lateinit var locationBtn: View

    private lateinit var placesClient: PlacesClient

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val binding = FragmentMapBinding.bind(view)
        rootMF = binding.root

        val mapViewMF = rootMF.mapViewMF
        mapViewMF.onCreate(savedInstanceState)
        mapViewMF.onResume()
        mapViewMF.getMapAsync(this)

        mFusedClient = LocationServices.getFusedLocationProviderClient(mActivity)
        Places.initialize(mContext, getString(R.string.API_Key_Petidence))
        placesClient = Places.createClient(mContext)

        //region Button Connections
        fun findPlace(placeType: String)
        {
            if (this::mLastLocation.isInitialized)
            {
                mRippleMF.startRippleAnimation()

                val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=${mLastLocation.latitude},${mLastLocation.longitude}" +
                        "&radius=10000" +
                        "&types=$placeType" +
                        "&sensor=true" +
                        "&key=" + getString(R.string.API_Key_Petidence)

                val task = PlaceTask()
                task.execute(url)
            }
            else
            {
                locationBtn.performClick()
            }
        }

        mRippleMF = rootMF.rippleMF

        rootMF.tvFindVetsMF.setOnClickListener {
            findPlace("veterinary_care")
        }

        rootMF.tvFindStoresMF.setOnClickListener {
            findPlace("pet_store")

            //region Pet Store Toast
            val petStoreToast = Toast.makeText(mContext, R.string.Store_Toast, Toast.LENGTH_LONG)
            petStoreToast.setGravity(Gravity.TOP, 0, 160)
            petStoreToast.show()
            //endregion
        }
        //endregion

        return view
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mGoogleMap = googleMap
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = true

        //region Location Button Position
        if (mapViewMF != null && mapViewMF.findViewById<View>(Integer.parseInt("1")) != null)
        {
            locationBtn = (mapViewMF.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
            val params = locationBtn.layoutParams as (RelativeLayout.LayoutParams)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
            params.setMargins(0,0,0,275);
        }
        //endregion

        //region GPS Control
        val locationReq = LocationRequest.create()
        locationReq.interval = 10000
        locationReq.fastestInterval = 5000
        locationReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationReq)
        val client = LocationServices.getSettingsClient(mActivity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            getDeviceLocation()
        }
        task.addOnFailureListener(mActivity) { e ->
            if (e is ResolvableApiException)
            {
                try
                {
                    e.startResolutionForResult(mActivity, REQUEST_CODE)
                }
                catch (e1: IntentSender.SendIntentException)
                {
                    e1.printStackTrace()
                }
            }
        }
        //endregion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                getDeviceLocation()
            }
        }
    }

    private fun getDeviceLocation()
    {
        mFusedClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                if (task.result != null)
                {
                    mLastLocation = task.result
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastLocation.latitude, mLastLocation.longitude), DEFAULT_ZOOM))
                }
                else
                {
                    val locationReq = LocationRequest.create()
                    locationReq.interval = 10000
                    locationReq.fastestInterval = 5000
                    locationReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                    locationCallback = object: LocationCallback()
                    {
                        override fun onLocationResult(locationResult: LocationResult?)
                        {
                            super.onLocationResult(locationResult)

                            if (locationResult == null)
                            {
                                return
                            }

                            mLastLocation = locationResult.lastLocation
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastLocation.latitude, mLastLocation.longitude), DEFAULT_ZOOM))
                            mFusedClient.removeLocationUpdates(locationCallback)
                        }
                    }
                    mFusedClient.requestLocationUpdates(locationReq, locationCallback, null)
                }
            }
            else
            {
                //region Location Toast
                val locationToast = Toast.makeText(mContext, R.string.Location_Toast, Toast.LENGTH_SHORT)
                locationToast.setGravity(Gravity.BOTTOM, 0, 160)
                locationToast.show()
                //endregion
            }
        }
    }

    private class PlaceTask: AsyncTask<String, Int, String>()
    {
        override fun doInBackground(vararg strings: String): String
        {
            return downloadURL(strings[0])
        }

        override fun onPostExecute(s: String?)
        {
            val task = ParserTask()
            task.execute(s)
        }

        @Throws(IOException::class)
        private fun downloadURL(string: String): String
        {
            val url = URL(string)
            val connection = url.openConnection()
            connection.connect()
            val stream = connection.getInputStream()
            val reader = BufferedReader(InputStreamReader(stream))
            val builder = StringBuilder()
            var line: String? = ""
            while ({ line = reader.readLine(); line != null }())
            {
                builder.append(line)
            }
            val data = builder.toString()
            reader.close()

            return data
        }
    }

    private class ParserTask: AsyncTask<String, Int, List<HashMap<String, String>>>()
    {
        override fun doInBackground(vararg strings: String): List<HashMap<String, String>>
        {
            val jsonParser = JsonParser()
            val jsonObject = JSONObject(strings[0])

            return jsonParser.parseResult(jsonObject)
        }

        override fun onPostExecute(hashMaps: List<HashMap<String, String>>?)
        {
            mGoogleMap.clear()

            val hashMapsSize = hashMaps!!.size
            for (i in 0 until hashMapsSize)
            {
                val hashMapList = hashMaps[i]
                val lat = hashMapList["lat"]!!.toDouble()
                val lng = hashMapList["lng"]!!.toDouble()
                val name = hashMapList["name"]
                val latLng = LatLng(lat, lng)
                val options = MarkerOptions()
                options.position(latLng)
                options.title(name)
                mGoogleMap.addMarker(options)
            }

            mRippleMF.stopRippleAnimation()
        }
    }
}

//PureBlack Software / Murat BIYIK