package com.example.githubapi

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun userSearch(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users")
    @Headers("Authorization: token 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")
    fun usersSearch(): Call<List<ItemsItem>>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun userDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun userFollower(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun userFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}