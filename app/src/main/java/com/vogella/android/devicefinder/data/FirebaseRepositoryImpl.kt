package com.vogella.android.devicefinder.data

import com.vogella.android.devicefinder.data.retrofit.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseRepositoryImpl (private val fireBaseDataSource: FireBaseDataSource,
                              private val apiService: ApiService): FirebaseRepository{


    override fun addDeviceToFireBase(device: Device): Completable {

       return fireBaseDataSource.addDeviceToFireBase(device)
    }

    override fun getDeviceListFromFireBase(): Single<List<Device>> {
      return fireBaseDataSource.getDeviceListFromFireBase()
    }

    override fun updateDeviceInFireBase(device: Device): Completable {
      return  fireBaseDataSource.updateDeviceInFireBase(device)
    }

    override fun getDeviceListFromFireBaseApi(): Single<List<Device>> {
       return  apiService.getListOfDevices()
    }
}