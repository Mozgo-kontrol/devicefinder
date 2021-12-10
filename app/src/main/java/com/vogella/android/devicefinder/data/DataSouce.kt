package com.vogella.android.devicefinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import com.vogella.android.devicefinder.AsyncTransformer
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import java.util.*

class DataSource(private val context: Context, resources: Resources) {

    private val TAG: String = DataSource::class.java.simpleName
    //init the list

    //private val jsonFileString = Utils.getJsonFromAssets(this.context!!, "devices.json")

   // private val initListFromJson = parseJSONString(jsonFileString)

   //      private val initialDeviceList = initListFromJson
    // Example 1
     @SuppressLint("CheckResult")
     private fun initDevicesList(context: Context, filename: String) : List<Device> {
         val result = mutableListOf<Device>()
         Observable.fromCallable{parseJSONString(Utils.getJsonFromAssets(context, filename))}
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe{list ->list!!.forEach { device -> result.add(device)}}

        return result
    }
/*
    private fun getDeviceObserver(): Observer<List<Device>> {
        return object : Observer<List<Device>> {
            override fun onNext(t: List<Device>) {
               // t.forEach { device -> initList.add(device)}
                Log.i(TAG, "add ${t.size} elements in the list")
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "subscribed on $d")
            }

            override fun onComplete() {
                Log.i(TAG, "All items are emitted!")
            }
            override fun onError(e: Throwable) {
                Log.i(TAG, "onError: " + e.message)
            }
        }
    }*/

   private val initialDeviceList = initDevicesList(this.context, "devices.json")
    private var deviceLiveData = MutableLiveData(initialDeviceList)

    fun addDeviceFromList(list : List<Device>){
        val currentList = deviceLiveData.value as MutableList<Device>
        list.forEach { currentList.add(it)}
        deviceLiveData.postValue(currentList)

    }

    fun removeDevice(device: Device){}


    private fun updateDeviceStatus(d: Device) : Status =
        when(d.employee.id.toInt()){
            1->{Status.Free}
            else->{Status.InUse}
        }

    fun updateDeviceEmployee(device: Device, employee: Employee) {
        val currentList = deviceLiveData.value

        if (currentList != null) {
            for(d in currentList){
                if(d.id==device.id){
                    Log.wtf(TAG, "${d.model} before ${d.employee.firstname } and Status ${d.currentStatus}")
                    d.employee = employee
                    d.currentStatus = updateDeviceStatus(d)

                    deviceLiveData.postValue(currentList)

                    Log.wtf(TAG, "${d.model} after ${d.employee.firstname } and Status ${d.currentStatus}")

                }
            }
        }

    }

    fun getDeviceList(): MutableLiveData<List<Device>?> = deviceLiveData

    fun getDeviceForId(id: Long): Device? {
        deviceLiveData.value?.let { device ->
            return device.firstOrNull { it.id == id }
        }
        return null
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: DataSource? = null
        fun getDataSource(context: Context, resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(context, resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}