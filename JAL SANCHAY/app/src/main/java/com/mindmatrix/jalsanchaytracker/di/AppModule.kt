package com.mindmatrix.jalsanchaytracker.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mindmatrix.jalsanchaytracker.BuildConfig
import com.mindmatrix.jalsanchaytracker.database.HarvestDao
import com.mindmatrix.jalsanchaytracker.database.JalSanchayDatabase
import com.mindmatrix.jalsanchaytracker.network.GeminiApi
import com.mindmatrix.jalsanchaytracker.network.WeatherApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JalSanchayDatabase =
        Room.databaseBuilder(context, JalSanchayDatabase::class.java, "jal_sanchay.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHarvestDao(database: JalSanchayDatabase): HarvestDao = database.harvestDao()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient, moshi: Moshi): WeatherApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideGeminiApi(okHttpClient: OkHttpClient, moshi: Moshi): GeminiApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.GEMINI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
}
