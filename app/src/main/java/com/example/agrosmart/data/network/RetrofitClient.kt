package com.example.agrosmart.data.network

import android.os.Build
import com.example.agrosmart.BuildConfig
import com.example.agrosmart.core.utils.classes.UnsafeOkhttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val URL_BASE = BuildConfig.API_URL

    private var retrofit: Retrofit? = null

    //aqui se crea el cliente http tomando como base de sus operaciones la interfaz que tiene los metodos
    // GET, POST, PUT, DELETE
    // la interfaz sera recommendationService
    @JvmStatic
    fun recommendationService(): RecommendationService {
        if (retrofit == null) {
            val okHttpClient: OkHttpClient?
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                okHttpClient = UnsafeOkhttpClient.getUnsafeOkHttpClient()
            } else {
                okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(
                            45,
                            TimeUnit.SECONDS
                    )
                    .readTimeout(
                            45,
                            TimeUnit.SECONDS
                    )
                    .writeTimeout(
                            45,
                            TimeUnit.SECONDS
                    )
                    .build()
            }

            retrofit = Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(RecommendationService::class.java)
    }
}
