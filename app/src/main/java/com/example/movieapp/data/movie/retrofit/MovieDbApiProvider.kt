package com.example.movieapp.data.movie.retrofit

import com.example.movieapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MovieDbApiProvider {

    companion object {
        @Volatile
        private var INSTANCE: MovieDbApi? = null

        @JvmStatic
        fun getMovieDbApi(): MovieDbApi {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = createMovieDbApi()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private fun createMovieDbApi(): MovieDbApi {
            class QueryParamInterceptor : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    val htt = request.url.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                        .build()
                    request = request.newBuilder().url(htt).build()
                    return chain.proceed(request)
                }
            }

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(QueryParamInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .build()
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val gsonConverterFactory = GsonConverterFactory.create(gson)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(okhttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build()
            return retrofit.create(MovieDbApi::class.java)
        }
    }


}