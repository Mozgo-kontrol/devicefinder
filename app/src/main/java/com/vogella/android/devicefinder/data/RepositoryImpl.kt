package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

 class RepositoryImpl(private val fileDataSource: FileDataSource,
                      private val firebaseRepositoryImpl: FirebaseRepositoryImpl
 ) : Repository {
    override fun loadDevicesListFromAssets(): Observable<List<Device>> {
        return fileDataSource.loadDevicesList()
    }
    override fun saveFile(jsonString: String): Completable {
        return fileDataSource.saveFile(jsonString)
    }
    override fun loadFile(): Single<String> {
        return fileDataSource.loadFile()
    }
    //FirebaseRepository
    override fun addToFireDataBase(device: Device) : Completable{
        return firebaseRepositoryImpl.addDeviceToFireBase(device)
    }

    override fun getDeviceListFromRemoteDataBase(): Single<List<Device>>{
       return firebaseRepositoryImpl.getDeviceListFromFireBase()
    }

     override fun updateDeviceInFireBase(device: Device): Completable {
        return firebaseRepositoryImpl.updateDeviceInFireBase(device)
     }


     override fun synchronize(): Completable {
        TODO("Not yet implemented")
   }

     override fun getDeviceListFromFireBaseApi(): Single<List<Device>> {
        return firebaseRepositoryImpl.getDeviceListFromFireBaseApi()
     }


 }