package com.thoughtworks.miniweibo.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thoughtworks.miniweibo.model.Comment
import com.thoughtworks.miniweibo.model.TimelinePost
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://10.0.2.2:3000"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeiboService {
    @GET("comments")
    suspend fun getComments(): List<Comment>

    @GET("home_timeline")
    suspend fun getTimeline(): List<TimelinePost>
}
/*
    public api object that exposes lazy initialized retrofit service
 */
object WeiboApi {
    val retrofitService : WeiboService by lazy { retrofit.create(WeiboService::class.java) }
}