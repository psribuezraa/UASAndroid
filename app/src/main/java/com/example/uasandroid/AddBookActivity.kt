package com.example.uasandroid

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.uasandroid.database.AppDatabase
import com.example.uasandroid.database.Book
import com.example.uasandroid.database.BookDao
import com.example.uasandroid.network.ApiService
import com.example.uasandroid.network.RetrofitClient
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var apiService: ApiService
    private lateinit var bookStatusSpinner: Spinner
    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        // Initialize Room database and API service
        database = AppDatabase.getInstance(this)
        apiService = RetrofitClient.apiService

        // UI Elements
        val bookTitleField = findViewById<EditText>(R.id.bookTitleField)
        val bookAuthorField = findViewById<EditText>(R.id.bookAuthorField)
        val bookRatingField = findViewById<EditText>(R.id.bookRatingField)
        bookStatusSpinner = findViewById(R.id.bookStatusSpinner)
        val saveBookButton = findViewById<Button>(R.id.saveBookButton)

        // Setup Spinner
        setupSpinner()

        // Handle Save Button Click
        saveBookButton.setOnClickListener {
            val title = bookTitleField.text.toString()
            val author = bookAuthorField.text.toString()
            val status = bookStatusSpinner.selectedItem.toString()
            val rating = bookRatingField.text.toString().toFloatOrNull()

            if (title.isBlank() || author.isBlank() || rating == null || rating < 1.0 || rating > 5.0) {
                Toast.makeText(this, "Please fill in all fields correctly!",
                    Toast.LENGTH_SHORT).show()
            } else {
                val newBook = Book(
                    title = title,
                    author = author,
                    status = status,
                    rating = rating
                )

                // Save to Room database
                lifecycleScope.launch {
                    database.bookDao().insertBook(newBook)
                    Toast.makeText(this@AddBookActivity, "Book saved!", Toast.LENGTH_SHORT).show()

                    // Sync with API
                    syncWithAPI(newBook)

                    // Finish activity and return to main
                    finish()

                }
            }
        }
    }

    // Setup status spinner with predefined options
    private fun setupSpinner() {
        val statuses = listOf("Reading", "To Read", "Finished")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bookStatusSpinner.adapter = adapter
    }

    // Sync the new book with the API
    private suspend fun syncWithAPI(book: Book) {
        try {
            apiService.addBook(book)
            Toast.makeText(this, "Book synced with server!",
                Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to sync with server: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }
}
