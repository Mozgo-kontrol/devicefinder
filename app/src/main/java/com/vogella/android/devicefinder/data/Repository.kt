package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
     //File
     fun loadDevicesList(filename: String): Observable<List<Device>>
     fun saveFile(jsonString : String) : Completable
     fun loadFile(): Single<String>
     //Firebase


}