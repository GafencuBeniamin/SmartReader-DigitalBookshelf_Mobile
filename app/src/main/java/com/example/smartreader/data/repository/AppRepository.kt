package com.example.SCD_proiect.data.repository

import com.example.scd_proiect_mobile_android.data.remote.AppRemoteDataSource
import javax.inject.Inject

class AppRepository @Inject constructor(
    remoteDataSource: AppRemoteDataSource,
){
    val remote = remoteDataSource
}