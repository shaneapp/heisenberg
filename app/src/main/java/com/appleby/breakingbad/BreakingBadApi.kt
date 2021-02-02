package com.appleby.breakingbad

import com.appleby.breakingbad.model.DataStore
import com.appleby.breakingbad.networkmodel.GoogleResult
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object BreakingBadApi {

    val google_custom_search_api = "AIzaSyAWnd173TUhyEbEdCCHUnoHPrm6TwN3_O8"
    val search_engine_id = "53cafac25d3e1c82b"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://customsearch.googleapis.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service by lazy {
        retrofit.create(BreakingBadApiService::class.java)
    }

    interface BreakingBadApiService {
        @GET("customsearch/v1")
        fun getGoogleImageResults(@Query("q") query : String,
                                  @Query("num") amount: Int,
                                  @Query("start") offset: Int,
                                  @Query("searchType") searchType : String = "image",
                                  @Query("imgSize") imageSize : String = DataStore.imageFilter,
                                  @Query("key") key : String = google_custom_search_api,
                                  @Query("cx") searchEngineId : String = search_engine_id) : Observable<Response<GoogleResult>>
    }

}