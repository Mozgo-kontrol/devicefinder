package com.vogella.android.devicefinder.data.retrofit

import com.vogella.android.devicefinder.data.Device
import retrofit2.Call
import retrofit2.http.*


interface Api {
    //hallo
   // createNewObjekt
    @POST("/{devices}.json")
    fun addDevice(@Path("devices") s1: String?, @Body device: Device?): Call<Device?>?

    @PUT("/devices/{deviceId}.json")
    fun updateDevice(@Path("deviceId") s1: String?, @Body device: Device?): Call<Device?>?

    @GET("/devices.json")
    fun getListOfDevice(): Call<HashMap<String,Device>>

     @GET("/devices/{deviceId}.json")
      fun getDeviceById(@Path("deviceId") s1: String): Call<Device>


  //  @PUT("/upload/{new}.json")
  //  fun setDataWithoutRandomness(@Path("new") s1: String?, @Body user: User?): Call<User?>?

}