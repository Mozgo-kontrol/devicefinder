package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Single

interface FirebaseRepository {
    fun addDeviceToFireBase(device: Device): Completable
    fun getDeviceListFromFireBase(): Single<List<Device>>
    fun updateDeviceInFireBase(device: Device): Completable
    fun getDeviceListFromFireBaseApi(): Single<List<Device>>
}