package com.vogella.android.devicefinder.data.retrofit

import android.annotation.SuppressLint
import android.util.Log
import com.vogella.android.devicefinder.data.Device
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://devicefinder-server-v1-default-rtdb.europe-west1.firebasedatabase.app/") //url of firebase app
        .addConverterFactory(GsonConverterFactory.create()) //use for convert JSON file into object
        .build()

    var api: Api = retrofit.create(Api::class.java) //use of interface


    fun addDevice(device: Device) {
        val call: Call<Device?>? = api.updateDevice(device.id.toString(), device)
        call?.enqueue(object : Callback<Device?> {
            override fun onResponse(call: Call<Device?>?, response: Response<Device?>?) {
                Log.d("device", "susses")
            }

            override fun onFailure(call: Call<Device?>?, t: Throwable?) {
                Log.d("device", "fail")
            }
        })
    }

    fun getDeviceById(string: String){
        val call = api.getDeviceById(string)
        call.enqueue(object :Callback<Device>{
            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                Log.d("devicebyId", "susses ${response.body().toString()}")
                print(response.body())
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                Log.d("devicebyId", "fail $t")
            }
        })
    }


   fun getListOfDevices() : Single<List<Device>>{
      return  Single.create { emitter ->
          var listOfDevices = mutableListOf<Device>()
          val call = api.getListOfDevice()
          call.enqueue( object : Callback<HashMap<String,Device>>{
              override fun onResponse(
                  call: Call<HashMap<String, Device>>,
                  response: Response<HashMap<String, Device>>
              ) {

                  response.body()!!.values.forEach {
                      listOfDevices.add(
                          Device(
                              it.id,
                              it.model,
                              it.employee,
                              it.currentStatus)
                      )  }
                  Log.d("list of devices", "susses $listOfDevices")
                  emitter.onSuccess(listOfDevices)

              }

              override fun onFailure(call: Call<HashMap<String, Device>>, t: Throwable) {
                  Log.d("list of devices", "fail $t")
              }
          })
      }
  }

    fun createNewDevice(device: Device) {
        val call: Call<Device?>? = api.addDevice("devices", device)
        call?.enqueue(object : Callback<Device?> {
            override fun onResponse(call: Call<Device?>?, response: Response<Device?>?) {
                Log.d("device", "susses");
            }

            override fun onFailure(call: Call<Device?>?, t: Throwable?) {
                Log.d("device", "fail");
            }
        })
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: ApiService? = null
        //const
        private const val FILE_NAME = ""
        private const val FILE_NAME_ASSETS = ""

        fun getApiService(): ApiService {
            return synchronized(ApiService::class) {
                val newInstance = INSTANCE ?: ApiService()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}