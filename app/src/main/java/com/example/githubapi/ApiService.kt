package com.example.githubapi

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    //@Headers("Authorization: token [your token]")
    fun userSearch(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users")
    //@Headers("Authorization: token [your token]")
    fun usersSearch(): Call<List<ItemsItem>>

    @GET("users/{username}")
    //@Headers("Authorization: token [your token]")
    fun userDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    //@Headers("Authorization: token [your token]")
    fun userFollower(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    //@Headers("Authorization: token [your token]")
    fun userFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}