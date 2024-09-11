package com.smh.unittest.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object RetrofitInitializer {

    @OptIn(ExperimentalSerializationApi::class)
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun createOkHttpClient(
        interceptors: List<Interceptor>,
    ) = OkHttpClient.Builder()
        .also {
            interceptors.forEach { interceptor ->
                it.addInterceptor(interceptor)
            }
            it.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .build()

    fun createRetrofitClient(url: String, okHttpClient: OkHttpClient, networkJson: Json):
            Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(NullOnEmptyConverterFactory())
        .build()

    private class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *> {
            val delegate: Converter<ResponseBody, *> =
                retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body: ResponseBody ->
                if (body.contentLength() == 0L) {
                    null
                } else {
                    delegate.convert(body)
                }
            }
        }
    }
}