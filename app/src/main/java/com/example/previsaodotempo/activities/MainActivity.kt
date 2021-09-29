package com.example.previsaodotempo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.previsaodotempo.Constants
import com.example.previsaodotempo.R
import com.example.previsaodotempo.models.*
import com.example.previsaodotempo.network.WeatherService
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mProgressDialog: Dialog? = null
    private lateinit var mSharedPreferences: SharedPreferences
    private var mListTotal: ArrayList<Minutely>? = null
    private var mHourList: ArrayList<Hourly>? = null
    private var mHourWeather: ArrayList<Weather>? = null
    private var mDailyList: ArrayList<Daily>? = null
    private var mDailyWeather: ArrayList<Weather>? = null
    private var mDailyTempList: ArrayList<Temp>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)





        btn_hour.setOnClickListener {
            val intent = Intent(this, HourActivity::class.java)
            intent.putExtra(HOUR, mHourList)
            intent.putExtra(WEATHER_HOUR, mHourWeather)
            startActivity(intent)

        }
        btn_day.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            intent.putExtra(DAILY, mDailyList)
            intent.putExtra(WEATHER_DAILY, mDailyWeather)
            intent.putExtra(TEMP_DAILY, mDailyTempList)
            startActivity(intent)
        }
        if (!isLocationEnabled()) {
            Toast.makeText(
                this,
                "Sua localização está desativada. Por favor atíve-a",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        requestLocationData()


                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(
                            this@MainActivity,
                            "Você negou acesso a sua localização. Por favor, habilite o acesso a localização.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()


        }
        btn_minute.setOnClickListener {


            val intent = Intent(this, MinuteActivity::class.java)
            intent.putExtra(MINUTE, mListTotal)
            startActivity(intent)

        }
    }

    //@SuppressLint("MissingPermission")
    @SuppressLint("MissingPermission")
    private fun requestLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY



        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val latitude = mLastLocation.latitude
            Log.i("Current Latitude", "$latitude")

            val longitude = mLastLocation.longitude
            Log.i("Current Longitude", "$longitude")
            getLocationWeatherDetails(latitude, longitude)
        }
    }

    private fun getLocationWeatherDetails(latitude: Double, longitude: Double) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: WeatherService =
                retrofit.create<WeatherService>(WeatherService::class.java)
            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude, longitude, Constants.METRIC_UNIT, Constants.AAP_ID, Constants.LANGUAGE
            )

            showCustomProgressDialog()

            listCall.enqueue(object : Callback<WeatherResponse> {

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        hideProgressDialog()
                        val weatherList: WeatherResponse? = response.body()

                        val weatherResponseJsonString = Gson().toJson(weatherList)
                        val editor = mSharedPreferences.edit()
                        editor.putString(Constants.WEATHER_RESPONSE_DATA, weatherResponseJsonString)
                        editor.apply()


                        setupUI()






                        Log.i("Response Result", "$weatherList")
                    } else {
                        val rc = response.code()
                        when (rc) {
                            400 -> {
                                Log.e("Error 400", " Bad connection")
                            }
                            404 -> {
                                Log.e("Error 404", " Not Found")
                            }
                            else -> {
                                Log.e("Error $", "Generic error")

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Errorrrrr", t.message.toString())
                    hideProgressDialog()
                }
            })
        } else {
            Toast.makeText(this@MainActivity, "Sem internet disponível.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("Parece que você desabilitou as permissões para acesso a localização, favor habilitar.")
            .setPositiveButton("CONFIGURAÇÕES") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)
        mProgressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }


    private fun setupUI() {

        val weatherResponseJsonString =
            mSharedPreferences.getString(Constants.WEATHER_RESPONSE_DATA, "")

        if (!weatherResponseJsonString.isNullOrEmpty()) {
            val weatherList =
                Gson().fromJson(weatherResponseJsonString, WeatherResponse::class.java)


            val mList = ArrayList<Minutely>()
            for (i in weatherList.minutely.indices) {
                val list = Minutely(
                    weatherList.minutely[i].dt,
                    weatherList.minutely[i].precipitation
                )
                mList.add(list)
            }
            mListTotal = mList

            val hourList = ArrayList<Hourly>()
            val hourWeatherList = ArrayList<Weather>()
            for (n in weatherList.hourly.indices) {
                for (j in weatherList.hourly[n].weather.indices) {
                    val list = Hourly(
                        weatherList.hourly[n].dt,
                        weatherList.hourly[n].temp,
                        weatherList.hourly[n].feels_like,
                        weatherList.hourly[n].humidity,
                        weatherList.hourly[n].weather

                    )
                    val weatherList = Weather(
                        weatherList.hourly[n].weather[j].id,
                        weatherList.hourly[n].weather[j].main,
                        weatherList.hourly[n].weather[j].description,
                        weatherList.hourly[n].weather[j].icon

                    )
                    hourWeatherList.add(weatherList)
                    hourList.add(list)
                }
                mHourList = hourList
                mHourWeather = hourWeatherList
            }

            val dailyList = ArrayList<Daily>()
            val dailyWeatherList = ArrayList<Weather>()
            val dailyTempList = ArrayList<Temp>()
            for (n in weatherList.daily.indices) {

                    val tempList = Temp(
                        weatherList.daily[n].temp.min,
                        weatherList.daily[n].temp.max
                    )
                    dailyTempList.add(tempList)


                for (j in weatherList.daily[n].weather.indices) {

                    val list = Daily(
                        weatherList.daily[n].dt,
                        weatherList.daily[n].temp,
                        weatherList.daily[n].humidity,
                        weatherList.daily[n].wind_speed,
                        weatherList.daily[n].wind_deg,
                        weatherList.daily[n].clouds,
                        weatherList.daily[n].uvi,
                        weatherList.daily[n].pop,
                        weatherList.daily[n].weather

                    )
                    val weatherList = Weather(
                        weatherList.daily[n].weather[j].id,
                        weatherList.daily[n].weather[j].main,
                        weatherList.daily[n].weather[j].description,
                        weatherList.daily[n].weather[j].icon

                    )
                    dailyWeatherList.add(weatherList)
                    dailyList.add(list)

                }
                mDailyList = dailyList
                mDailyWeather = dailyWeatherList
                mDailyTempList = dailyTempList
            }







            for (z in weatherList.current.weather.indices) {


                val temp = weatherList.current.temp.roundToInt()

                tv_main.text = weatherList.current.weather[z].description.capitalize()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_temp.text =
                        temp.toString() + getUnit(application.resources.configuration.locales[0].country.toString())
                } else {
                    tv_temp.text =
                        temp.toString() + getUnit(application.resources.configuration.locale.country.toString())
                }





                tv_sunrise.text = (unixTime(weatherList.current.sunrise)).toString()
                tv_sunset.text = (unixTime(weatherList.current.sunset)).toString()
                tv_humidity.text = weatherList.current.humidity.toString() + " %"
                val feel = weatherList.current.feels_like.roundToInt()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_feel_like.text =
                        feel.toString() + getUnit(application.resources.configuration.locales[0].country.toString())
                } else {
                    tv_feel_like.text =
                        feel.toString() + getUnit(application.resources.configuration.locale.country.toString())
                }

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_min.text =
                        weatherList.main.temp_min.toString() + getUnit(application.resources.configuration.locales[0].country.toString()) + " min"
                } else {
                    tv_min.text =
                        weatherList.main.temp_min.toString() + getUnit(application.resources.configuration.locale.country.toString()) + " min"
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_max.text =
                        weatherList.main.temp_max.toString() + getUnit(application.resources.configuration.locales[0].country.toString()) + " max"
                } else {
                    tv_max.text =
                        weatherList.main.temp_max.toString() + getUnit(application.resources.configuration.locale.country.toString()) + " max"
                }*/

                var geocoder = Geocoder(this, Locale.getDefault())
                val location: List<Address> =
                    geocoder.getFromLocation(weatherList.lat, weatherList.lon, 1)

                var cityName = location[0].subAdminArea

                var countryName = location.get(0).countryName

                var stateName = location[0].adminArea

                val speed = (weatherList.current.wind_speed * 3.6).roundToInt()


                tv_local.text = "$cityName, $stateName - $countryName"
                tv_speed.text = speed.toString() + " km/h"


                val uv = weatherList.current.uvi.roundToInt()
                var indiceUv: String = ""
                if (uv < 3) {
                    indiceUv = "BAIXO"
                } else if (uv < 6) {
                    indiceUv = "MODERADO"
                } else if (uv < 8) {
                    indiceUv = "ALTO"
                } else if (uv < 11) {
                    indiceUv = "MUITO ALTO"
                } else {
                    indiceUv = "EXTREMO"
                }
                tv_uvi.text = uv.toString()

                tv_indice_uv.text = indiceUv


                if (weatherList.current.wind_deg < 11.26) {
                    tv_wind_direction.text = "Norte"
                } else if (weatherList.current.wind_deg < 33.76) {
                    tv_wind_direction.text = "Norte-nordeste"
                } else if (weatherList.current.wind_deg < 56.26) {
                    tv_wind_direction.text = "Nordeste"
                } else if (weatherList.current.wind_deg < 78.76) {
                    tv_wind_direction.text = "Este-nordeste"
                } else if (weatherList.current.wind_deg < 101.76) {
                    tv_wind_direction.text = "Leste"
                } else if (weatherList.current.wind_deg < 123.76) {
                    tv_wind_direction.text = "Este-sudeste"
                } else if (weatherList.current.wind_deg < 146.76) {
                    tv_wind_direction.text = "Sudeste"
                } else if (weatherList.current.wind_deg < 168.76) {
                    tv_wind_direction.text = "Sul-sudeste"
                } else if (weatherList.current.wind_deg < 191.76) {
                    tv_wind_direction.text = "Sul"
                } else if (weatherList.current.wind_deg < 213.76) {
                    tv_wind_direction.text = "Sul-sudoeste"
                } else if (weatherList.current.wind_deg < 236.76) {
                    tv_wind_direction.text = "Sudoeste"
                } else if (weatherList.current.wind_deg < 258.76) {
                    tv_wind_direction.text = "Oeste-sudoeste"
                } else if (weatherList.current.wind_deg < 281.76) {
                    tv_wind_direction.text = "Oeste"
                } else if (weatherList.current.wind_deg < 303.76) {
                    tv_wind_direction.text = "Oeste-noroeste"
                } else if (weatherList.current.wind_deg < 326.76) {
                    tv_wind_direction.text = "Noroeste"
                } else if (weatherList.current.wind_deg < 348.76) {
                    tv_wind_direction.text = "Norte-noroeste"
                } else {
                    tv_wind_direction.text = "Norte"
                }

                when (weatherList.current.weather[z].icon) {
                    "01d" -> iv_main.setImageResource(R.drawable.ic_01d)
                    "02d" -> iv_main.setImageResource(R.drawable.ic_02d)
                    "03d" -> iv_main.setImageResource(R.drawable.ic_03d)
                    "04d" -> iv_main.setImageResource(R.drawable.ic_04d)
                    "09d" -> iv_main.setImageResource(R.drawable.ic_09d)
                    "10d" -> iv_main.setImageResource(R.drawable.ic_10d)
                    "11d" -> iv_main.setImageResource(R.drawable.ic_11d)
                    "13d" -> iv_main.setImageResource(R.drawable.ic_13d)
                    "50d" -> iv_main.setImageResource(R.drawable.ic_50d)

                    "01n" -> iv_main.setImageResource(R.drawable.ic_01n)
                    "02n" -> iv_main.setImageResource(R.drawable.ic_02n)
                    "03n" -> iv_main.setImageResource(R.drawable.ic_03n)
                    "04n" -> iv_main.setImageResource(R.drawable.ic_04n)
                    "09n" -> iv_main.setImageResource(R.drawable.ic_09n)
                    "10n" -> iv_main.setImageResource(R.drawable.ic_10n)
                    "11n" -> iv_main.setImageResource(R.drawable.ic_11n)
                    "13n" -> iv_main.setImageResource(R.drawable.ic_13n)
                    "50n" -> iv_main.setImageResource(R.drawable.ic_50d)

                }

            }
        }
        // For loop to get the required data. And all are populated in the UI.

    }


    private fun getUnit(value: String): String? {
        var value = "°C"
        if ("US" == value || "LR" == value || "MM" == value) {
            value = "°F"
        }
        return value

    }

    private fun unixTime(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {

                requestLocationData()

                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    companion object {
        var MINUTE = "minute"
        var HOUR = "hour"
        var WEATHER_HOUR = "weather_hour"
        var DAILY = "daily"
        var WEATHER_DAILY = "weather_daily"
        var TEMP_DAILY = "temp_daily"
    }


}