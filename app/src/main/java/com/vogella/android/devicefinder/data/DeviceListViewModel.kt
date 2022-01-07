package com.vogella.android.devicefinder.data

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * VieModel class responsible for business logic in the App
 *
 * and word LifeData class
 *
 *
 * */

class DeviceListViewModel(
    private val fileDataSource: FileDataSource,
    private val fireBaseDataSource: FireBaseDataSource
    ) : ViewModel() {

    private val TAG: String = DeviceListViewModel::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()

    private val deviceListLiveData: MutableLiveData<List<Device>> = MutableLiveData()

    init {
        loadDevicesFromFile()
    }

    private fun loadInitListFromAssets() {
        fileDataSource.loadDevicesList()
            .subscribeOn(Schedulers.io())// who runs method loadDevicesList
            .observeOn(AndroidSchedulers.mainThread())//who added list to LifeData
            .subscribe { list ->
                deviceListLiveData.value = list
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun saveDevicesToFile(devices: List<Device>) {

        parseListToJsonString(devices).
            flatMapCompletable{fileDataSource.saveFile(it) }
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

       fileDataSource.loadFile()
            .subscribeOn(Schedulers.io())
            .flatMap{ parseJSONSStringToList2(it) }  //??
            .observeOn(AndroidSchedulers.mainThread())
           // .onErrorReturn {deviceList(dataSource.resources)}//if error
            .subscribe {
             list -> deviceListLiveData.value = list
                Log.i(
                    TAG,
                    "data is laded from the local File"
                )
            }
            .also {
                compositeDisposable.add(it)
            }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun addNewDevice(modelName : String) {
        val device = Device(
            findMaxId()+1 ,
            modelName,
            imOfficeEmployee(),
            Status.Free
        )

        val currentList = deviceListLiveData.value as MutableList
        currentList.add(device)
        deviceListLiveData.postValue(currentList)
        saveDevicesToFile(currentList)

    }

    private fun findMaxId() : Long {
        var max :Long = 0
        val currentList = deviceListLiveData.value as MutableList
         currentList.forEach {(if(it.id > max) max = it.id)}
        return max
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
                    saveDevicesToFile(currentList)
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
                fileDataSource = FileDataSource.getFileDataSource(context, context.resources),
                fireBaseDataSource = FireBaseDataSource.getFireBaseDataSource(context, context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}