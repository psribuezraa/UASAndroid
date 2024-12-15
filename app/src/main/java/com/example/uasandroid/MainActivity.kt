package com.example.uasandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasandroid.database.AppDatabase
import com.example.uasandroid.database.Book
import com.example.uasandroid.network.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var bookAdapter: BookAdapter
    private val bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getInstance(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(bookList,
            onBookClick = { book ->
                // Handle book click if needed
            },
            onDeleteClick = { book ->
                lifecycleScope.launch {
                    try {
                        // Delete book from database
                        database.bookDao().deleteBook(book)
                        // Remove book from list and notify adapter
                        bookList.remove(book)
                        bookAdapter.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity, "Book Deleted", Toast.LENGTH_SHORT)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error deleting book: ${e.message}")
                    }
                }
            }
        )
        recyclerView.adapter = bookAdapter

        val fabAddBook = findViewById<FloatingActionButton>(R.id.fabAddBook)
        fabAddBook.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        // Initial load
        loadBooks()
    }

    override fun onResume() {
        super.onResume()
        // Reload data when returning from AddBookActivity
        loadBooks()
    }

    private fun loadBooks() {
        lifecycleScope.launch {
            try {
                val books = database.bookDao().getAllBooks()
                bookList.clear()
                bookList.addAll(books)
                bookAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading books: ${e.message}")
            }
        }
    }

    fun refresh() {
        val client = RetrofitClient.apiService
        val books = client.getBooks()

        books.enqueue(object : Callback<List<Book>> {
            override fun onResponse(p0: Call<List<Book>>, p1: Response<List<Book>>) {
                PreferenceHelper(this@MainActivity).saveData(p1.body()!!)
                println(p1.body()!!.toString())
            }

            override fun onFailure(p0: Call<List<Book>>, p1: Throwable) {
                // Toast.makeText(binding.root.context, "Fetch Failed", Toast.LENGTH_SHORT.show())
            }
        })
    }
}
