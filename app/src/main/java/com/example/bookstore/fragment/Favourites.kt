package com.example.bookstore.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.example.bookstore.R
import com.example.bookstore.adapter.FavouritesRecyclerAdapter
import com.example.bookstore.database.BookDao
import com.example.bookstore.database.BookDatabase
import com.example.bookstore.database.BookEntity

class Favourites : Fragment() {
    lateinit var recyclerfavourites:RecyclerView
    lateinit var favouriteslayout: LayoutManager
    lateinit var favouritesadapter:FavouritesRecyclerAdapter
    lateinit var progresslayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    var booklist = listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerfavourites = view.findViewById(R.id.favouritesrecycler)
        favouriteslayout = GridLayoutManager(activity as Context,2)
        progresslayout = view.findViewById(R.id.progresslayout)
        progressBar = view.findViewById(R.id.progressbar)

        booklist = RetriveContent(activity as Context).execute().get()
        if (activity!=null){
            progresslayout.visibility = View.GONE
            favouritesadapter = FavouritesRecyclerAdapter(activity as Context,booklist)
            recyclerfavourites.adapter = favouritesadapter
            recyclerfavourites.layoutManager = favouriteslayout
        }

        return view
    }

    class RetriveContent(val context:Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()
            return db.bookDao().getAllBooks()
        }

    }


}