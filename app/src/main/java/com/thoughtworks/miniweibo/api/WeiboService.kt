package com.thoughtworks.miniweibo.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://localhost:3000/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeiboService {
    @GET("comments")
    fun getComments():
            Call<String>
}

/*
    public api object that exposes lazy initialized retrofit service
 */
object WeiboApi {
    val retrofitService : WeiboService by lazy { retrofit.create(WeiboService::class.java) }
}