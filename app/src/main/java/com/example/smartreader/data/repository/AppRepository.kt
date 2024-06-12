package com.example.smartreader.data.repository

import com.example.smartreader.data.remote.AppRemoteDataSource
import javax.inject.Inject

class AppRepository @Inject constructor(
    remoteDataSource: AppRemoteDataSource,
){
    val remote = remoteDataSource
}