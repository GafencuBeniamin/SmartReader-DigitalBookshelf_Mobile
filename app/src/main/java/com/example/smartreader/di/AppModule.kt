package com.example.smartreader.di


import com.example.scd_proiect_mobile_android.data.remote.AppRemoteDataSource
import com.example.scd_proiect_mobile_android.data.remote.AppService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): AppService =
        retrofit.create(AppService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: AppService) =
        AppRemoteDataSource(characterService)
}
