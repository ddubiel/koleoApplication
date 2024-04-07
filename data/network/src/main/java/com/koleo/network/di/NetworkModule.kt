package com.koleo.network.di

import com.koleo.network.KoleoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun getHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
           level = HttpLoggingInterceptor.Level.BODY //else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideOkHttpClientForRetrofit(/*retrofitClientInterceptor: RetrofitClientInterceptor*/): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(getHttpLoggingInterceptor())
        // builder.networkInterceptors().add(retrofitClientInterceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("https://koleo.pl")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideKoleoApi(
        retrofit: Retrofit
    ): KoleoApi {
        return retrofit.create(KoleoApi::class.java)
    }
}