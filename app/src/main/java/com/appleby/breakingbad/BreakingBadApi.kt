package com.appleby.breakingbad

import com.appleby.breakingbad.networkmodel.Character
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object BreakingBadApi {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://breakingbadapi.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service by lazy {
        retrofit.create(BreakingBadApiService::class.java)
    }

    interface BreakingBadApiService {
        @GET("api/characters")
        fun getCharacters() : Observable<Response<List<Character>>>
    }

}