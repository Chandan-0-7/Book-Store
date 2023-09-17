package com.example.bookstore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.activity.DescriptionActivity
import com.example.bookstore.dataclass.Book
import com.squareup.picasso.Picasso


class DashboardRecycleAdapter(private val context:Context, private val itemList: ArrayList<Book>): RecyclerView.Adapter<DashboardRecycleAdapter.DashboardHolder>() {


    class DashboardHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtbookname: TextView = view.findViewById(R.id.txtBookName)
        val txtauthor: TextView = view.findViewById(R.id.txtBookAuthor)
        val txtprice: TextView = view.findViewById(R.id.txtBookPrice)
        val txtrating:TextView = view.findViewById(R.id.txtBookRating)
        val txtbookimg:ImageView = view.findViewById(R.id.imgBookImage)
        val l1:LinearLayout= view.findViewById(R.id.l1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_single_row,parent,false)
        return DashboardHolder(view)
    }

    override fun getItemCount() : Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardHolder, position: Int) {
        val book = itemList[position]
        holder.txtbookname.text=book.bookName
        holder.txtauthor.text=book.bookAuthor
        holder.txtrating.text=book.bookRating
        holder.txtprice.text=book.bookPrice
        Glide.with(context).load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtbookimg)
       // holder.txtbookimg.setImageResource(book.bookImage)

        holder.l1.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.book_id)
            context.startActivity(intent)
        }
    }
}