package com.vogella.android.devicefinder.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeviceListViewModel(private val dataSource: DataSource) : ViewModel() {

    val deviceLiveData = dataSource.getDeviceList()
    /**
     *
     * VieModel class responsible for business logic in the App
     *
     * and word LifeData class
     *
     *
     * */
    fun addNewDevice(device: Device) {
       // dataSource.addDevice()
    }
    fun addNewDevicesList(list :List<Device>) {
        dataSource.addDeviceFromList(list)
    }
    fun updateDeviceEmployee(device: Device, employee: Employee) {
       dataSource.updateDeviceEmployee(device, employee)
    }

    fun removeEmployee(device: Device) {
       // dataSource.removeDevice()
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
class DevicesListViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceListViewModel(
                dataSource = DataSource.getDataSource(context,context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}