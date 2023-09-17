package com.example.bookstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.database.BookEntity
import java.util.zip.Inflater

class FavouritesRecyclerAdapter(val context: Context,val booklist:List<BookEntity>):RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>() {

    class FavouritesViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtbookname:TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtbookauthor:TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtbookprice:TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtbookrating:TextView = view.findViewById(R.id.txtFavBookRating)
        val txtbookimage:ImageView = view.findViewById(R.id.imgFavBookImage)
        val linearl1:LinearLayout = view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourites,parent,false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booklist.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val book = booklist[position]
        holder.txtbookname.text = book.bookName
        holder.txtbookauthor.text = book.bookAuthor
        holder.txtbookprice.text = book.bookPrice
        holder.txtbookrating.text = book.bookRating
        Glide.with(context).load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtbookimage)
    }
}