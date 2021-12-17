package com.vogella.android.devicefinder.data

import android.content.Context
import android.os.Build
import android.util.AndroidException
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vogella.android.devicefinder.AsyncTransformer
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * VieModel class responsible for business logic in the App
 *
 * and word LifeData class
 *
 *
 * */

class DeviceListViewModel(private val dataSource: DataSource) : ViewModel() {

    private val TAG: String = DataSource::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()
    private val deviceListLiveData: MutableLiveData<List<Device>> = MutableLiveData()

    init {
        if (dataSource.loadFile().isNotEmpty()) {
            loadDevicesFromFile()
        } else {
            loadInitListFromAssets()
        }
    }

    private fun loadInitListFromAssets() {
        dataSource.loadDevicesList()
            .subscribeOn(Schedulers.io())// who runs method loadDevicesList
            .observeOn(AndroidSchedulers.mainThread())//who added list to LifeData
            .subscribe { list ->
                deviceListLiveData.value = list
            }.also {
                compositeDisposable.add(it)
            }

    }

    fun saveDevicesToFile(devices: List<Device>) {

        Observable.fromCallable {
            val data = parseListToJsonString(devices)
            dataSource.saveFile(data)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Log.i(
                TAG,
                "data is saved to the File"
            ) }
            .also {
                compositeDisposable.add(it)
            }
    }

    private fun loadDevicesFromFile() {

        Observable.fromCallable {dataSource.loadFile()}
            .subscribeOn(Schedulers.io())
            .map {parseJSONSStringToList2(it)}  //??
            .observeOn(AndroidSchedulers.mainThread())
           // .onErrorReturn {deviceList(dataSource.resources)}//if error
            .doOnComplete {  Log.i(
                TAG,
                "data is laded from the local File"
            ) }
            .subscribe {
             list -> deviceListLiveData.value = list
            }
            .also {
                compositeDisposable.add(it)
            }
    }

    //example 1
    /*dataSource.loadFile()
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.newThread())
    .map {parseJSONSStringToList2(it)}
    .observeOn(AndroidSchedulers.mainThread())
    .doOnComplete {  Log.i(
        TAG,
        "data is laded from the local File"
    ) }
    .subscribe {
        list -> deviceListLiveData.value = list
    }
    .also {
        compositeDisposable.add(it)
    }*/





    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun addNewDevice(device: Device) {
        // dataSource.addDevice()
    }

    fun addNewDevicesList(list: List<Device>) {
        val currentList = deviceListLiveData.value as MutableList<Device>
        list.forEach { currentList.add(it) }
        deviceListLiveData.postValue(currentList)

    }

    fun updateDeviceEmployee(device: Device, employee: Employee) {
        val currentList = deviceListLiveData.value

        if (currentList != null) {
            for (d in currentList) {
                if (d.id == device.id) {
                    Log.wtf(
                        TAG,
                        "${d.model} before ${d.employee.firstname} and Status ${d.currentStatus}"
                    )
                    d.employee = employee
                    d.currentStatus = updateDeviceStatus(d)

                    deviceListLiveData.postValue(currentList!!)//??

                    Log.wtf(
                        TAG,
                        "${d.model} after ${d.employee.firstname} and Status ${d.currentStatus}"
                    )

                }
            }
        }

    }

    private fun updateDeviceStatus(d: Device): Status =
        when (d.employee.id.toInt()) {
            1 -> {
                Status.Free
            }
            else -> {
                Status.InUse
            }
        }

    fun removeEmployee(device: Device) {}

    fun getDeviceList(): MutableLiveData<List<Device>> = deviceListLiveData

    fun getDeviceForId(id: Long): Device? {
        deviceListLiveData.value?.let { device ->
            return device.firstOrNull { it.id == id }
        }
        return null
    }

}

/**
 *
 * VieModel Factory class responsible init the
 *
 * and word LifeData class
 *
 *
 * */
class DevicesListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceListViewModel(
                dataSource = DataSource.getDataSource(context, context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}