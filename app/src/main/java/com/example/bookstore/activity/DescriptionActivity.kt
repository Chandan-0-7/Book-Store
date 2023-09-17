package com.example.bookstore.activity

import android.content.Context
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstore.R
import com.example.bookstore.R.*
import com.example.bookstore.database.BookDatabase
import com.example.bookstore.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtbookname:TextView
    lateinit var txtbookauthor:TextView
    lateinit var txtbookprice:TextView
    lateinit var txtbookrating:TextView
    lateinit var txtbookimage:ImageView
    lateinit var txtdescription:TextView
    lateinit var addfavourites: Button
    lateinit var progresslayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var bookId:String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_description)
        txtbookname = findViewById(R.id.bookname)
        txtbookauthor = findViewById(R.id.authorname)
        txtbookprice = findViewById(R.id.bookprice)
        txtbookrating = findViewById(R.id.rating)
        txtbookimage = findViewById(R.id.bookphoto)
        txtdescription = findViewById(R.id.description)
        addfavourites = findViewById(R.id.add)
        progresslayout = findViewById(R.id.progresss)
        progresslayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressbarr)
        progressBar.visibility = View.VISIBLE
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        }
        else{
            Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_LONG).show()
        }
        if(bookId == "100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_LONG).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val post_api = "http://13.235.250.119/v1/book/get_book/"

        val jsonparams = JSONObject()
        jsonparams.put("book_id",bookId)

        val jsonRequest = object : JsonObjectRequest(Method.POST,post_api,jsonparams, Response.Listener {
            try {
                val success = it.getBoolean("success")
                if(success){
                    val jsonbookobject = it.getJSONObject("book_data")
                    progresslayout.visibility = View.GONE

                    val bookimgurl = jsonbookobject.getString("image")

                    Picasso.get().load(jsonbookobject.getString("image"))
                        .error(R.drawable.default_book_cover).into(txtbookimage)
                    txtbookname.text = jsonbookobject.getString("name")
                    txtbookauthor.text = jsonbookobject.getString("author")
                    txtbookrating.text = jsonbookobject.getString("rating")
                    txtbookprice.text = jsonbookobject.getString("price")
                    txtdescription.text = jsonbookobject.getString("description")

                    val bookEntity = BookEntity(
                        bookId?.toInt() as Int,
                        txtbookname.text.toString(),
                        txtbookauthor.text.toString(),
                        txtbookrating.text.toString(),
                        txtbookprice.text.toString(),
                        txtdescription.text.toString(),
                        bookimgurl
                    )

                    val checkFav = DbAsyncTask(applicationContext,bookEntity,1).execute()
                    val isFav = checkFav.get()
                    if(isFav){
                        addfavourites.text = "Remove From Favourites"
                        val favcolor = ContextCompat.getColor(applicationContext,R.color.favourites)
                        addfavourites.setBackgroundColor(favcolor)
                    }
                    else{
                        addfavourites.text = "Add To Favourites"
                        val nofavcolor = ContextCompat.getColor(applicationContext,R.color.drawer)
                        addfavourites.setBackgroundColor(nofavcolor)
                    }
                    addfavourites.setOnClickListener {
                        if(!DbAsyncTask(applicationContext,bookEntity,1).execute().get()){
                            val async = DbAsyncTask(applicationContext,bookEntity,2).execute()
                            val result = async.get()
                            if(result){
                                Toast.makeText(this@DescriptionActivity,"Book added to favourites",Toast.LENGTH_LONG).show()
                                addfavourites.text = "Remove From Favourites"
                                val favcolor = ContextCompat.getColor(applicationContext,R.color.favourites)
                                addfavourites.setBackgroundColor(favcolor)
                            }
                            else{
                                Toast.makeText(this@DescriptionActivity,"Try after sometime",Toast.LENGTH_LONG).show()

                            }
                        }
                        else{
                            val async = DbAsyncTask(applicationContext,bookEntity,3).execute()
                            val result = async.get()
                            if(result){
                                Toast.makeText(this@DescriptionActivity,"Book removed from favourites",Toast.LENGTH_LONG).show()
                                addfavourites.text = "Added To Favourites"
                                val nofavcolor = ContextCompat.getColor(applicationContext,R.color.drawer)
                                addfavourites.setBackgroundColor(nofavcolor)
                            }
                            else{
                                Toast.makeText(this@DescriptionActivity,"Try after sometime",Toast.LENGTH_LONG).show()

                            }

                        }
                    }


                }
                else{
                    Toast.makeText(this@DescriptionActivity,"some unexpected error occured",Toast.LENGTH_LONG).show()
                }
            }
            catch (e:Exception){
                Toast.makeText(this@DescriptionActivity,"Data unabled to fetch",Toast.LENGTH_LONG).show()

            }


        }, Response.ErrorListener {
            Toast.makeText(this@DescriptionActivity,"Some error occured",Toast.LENGTH_LONG).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "8b46c31c0fb959"
                return headers
            }

        }
        queue.add(jsonRequest)

    }

   class DbAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int): AsyncTask<Void, Void, Boolean>(){

        /*
        mode = 1->check db book is there or not
        mode = 2->insert the book in favourite
        mode = 3->remove the book from favourite
        */
        val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1->{
                    // mode = 1->check db book is there or not
                    val book:BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }
                2->{
                    // mode = 2->insert the book in favourites
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    // mode = 3->remove the book from favourites
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

}