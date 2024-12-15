package com.example.uasandroid.network

import com.example.uasandroid.database.Book
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("MVvdj/books")
    suspend fun addBook(@Body book: Book)

    @GET("MVvdj/books")
    fun getBooks(): Call<List<Book>>

    @PUT("MVvdj/books/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: Book)

    @DELETE("MVvdj/books/{id}")
    suspend fun deleteBook(@Path("id") id: Int)

//    @POST("https://ppbo-api.vercel.app/MVvdj/")
//    suspend fun addBook(@Body book: Book)
//
//    @GET("https://ppbo-api.vercel.app/MVvdj/")
//    suspend fun getBooks(): List<Book>
//
//    @PUT("https://ppbo-api.vercel.app/MVvdj/")
//    suspend fun updateBook(@Path("id") id: Int, @Body book: Book)
//
//    @DELETE("https://ppbo-api.vercel.app/MVvdj/")
//    suspend fun deleteBook(@Path("id") id: Int)
}