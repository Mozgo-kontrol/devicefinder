package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
     //File
     fun loadDevicesListFromAssets() : Observable<List<Device>>
     fun saveFile(jsonString : String) : Completable
     fun loadFile(): Single<String>
     //Firebase
     fun addToFireDataBase(device : Device) : Completable
     fun getDeviceListFromRemoteDataBase() : Single<List<Device>>
     fun updateDeviceInFireBase(device: Device): Completable
     //common
     fun synchronize(): Completable
     fun getDeviceListFromFireBaseApi() : Single<List<Device>>
}