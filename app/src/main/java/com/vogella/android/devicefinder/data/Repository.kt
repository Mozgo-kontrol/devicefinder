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
     fun getDeviceListFromRemoteDataBase() : Observable<List<Device>>
     //common
     fun synchronize(): Completable
     fun cleanListener()
}