package com.example.uasandroid.database

import android.media.Rating
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "books")

public final data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val status: String, // bisa "Reading", "To Read", "Finished"
    val rating: Float
)
