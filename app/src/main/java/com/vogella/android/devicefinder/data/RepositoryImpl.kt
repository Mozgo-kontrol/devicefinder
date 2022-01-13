package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RepositoryImpl (private val fileDataSource: FileDataSource,
                      private val fireBaseDataSource: FireBaseDataSource) : Repository {

    override fun loadDevicesListFromAssets(): Observable<List<Device>> {
        return fileDataSource.loadDevicesList()
    }
    override fun saveFile(jsonString: String): Completable {
        return fileDataSource.saveFile(jsonString)
    }
    override fun loadFile(): Single<String> {
        return fileDataSource.loadFile()
    }

    override fun addToFireDataBase(device: Device) : Completable{
        return fireBaseDataSource.addDeviceToFireBase(device)
    }

    override fun getDeviceListFromRemoteDataBase(): Observable<List<Device>>{
       return fireBaseDataSource.getDeviceListFromFireBase()
    }

    override fun synchronize(): Completable {
        TODO("Not yet implemented")
    }

    override fun cleanListener() {
        fireBaseDataSource.cleanListener()
    }

}