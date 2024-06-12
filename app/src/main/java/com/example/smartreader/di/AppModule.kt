package com.example.smartreader.di


import android.content.Context
import com.example.smartreader.data.remote.AppRemoteDataSource
import com.example.smartreader.data.remote.AppService
import com.example.smartreader.MainApplication.Companion.context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
        .client(provideOkHttpClient(context))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    @Provides
    fun provideAppService(retrofit: Retrofit): AppService =
        retrofit.create(AppService::class.java)

    @Singleton
    @Provides
    fun provideAppRemoteDataSource(characterService: AppService) =
        AppRemoteDataSource(characterService)
}
