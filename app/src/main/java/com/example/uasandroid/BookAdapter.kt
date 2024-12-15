package com.example.uasandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasandroid.database.Book

class BookAdapter(
    private val books: List<Book>,
    private val onBookClick: (Book) -> Unit,
    private val onDeleteClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.bookTitle)
        val author: TextView = view.findViewById(R.id.bookAuthor)
        val status: TextView = view.findViewById(R.id.bookStatus)
        val rating: TextView = view.findViewById(R.id.bookRating)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteBookButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.title
        holder.author.text = book.author
        holder.status.text = book.status
        holder.rating.text = "Rating: ${book.rating}"
        holder.itemView.setOnClickListener { onBookClick(book) }
        holder.deleteButton.setOnClickListener {
            onDeleteClick(book)
        }
    }

    override fun getItemCount() = books.size
}