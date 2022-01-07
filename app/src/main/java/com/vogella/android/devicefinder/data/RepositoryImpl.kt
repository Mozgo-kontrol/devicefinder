package com.vogella.android.devicefinder.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RepositoryImpl (private val fileDataSource: FileDataSource,
                      private val fireBaseDataSource: FireBaseDataSource) : Repository {

    override fun loadDevicesList(filename: String): Observable<List<Device>> {
        return fileDataSource.loadDevicesList()
    }

    override fun saveFile(jsonString: String): Completable {
        return fileDataSource.saveFile(jsonString)
    }

    override fun loadFile(): Single<String> {
        return fileDataSource.loadFile()
    }

}