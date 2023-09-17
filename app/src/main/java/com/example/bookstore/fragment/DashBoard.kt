package com.example.bookstore.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.bookstore.adapter.DashboardRecycleAdapter
import com.example.bookstore.R
import com.example.bookstore.dataclass.Book
import org.json.JSONException
import java.util.Collections
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest

class DashBoard : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var dashboardRecycleAdapter: DashboardRecycleAdapter
    val booklist = arrayListOf<Book>()
    lateinit var progresslayout:RelativeLayout
    lateinit var progressBar: ProgressBar

    var bookcompare = Comparator<Book>{
            book1,book2->
        if(book1.bookRating.compareTo(book2.bookRating,ignoreCase = true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }
        else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_dash_board, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        progresslayout = view.findViewById(R.id.progresslayout)
        progressBar = view.findViewById(R.id.progressbar)
        layoutmanager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)

        progresslayout.visibility = View.VISIBLE



        val queue = Volley.newRequestQueue(activity as Context)
        val testapi = "http://13.235.250.119/v1/book/fetch_books/"





        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,testapi,null,
            Response.Listener {
                try{
                    progresslayout.visibility=View.GONE
                    val success = it.getBoolean("success")
                    if(success){
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image"))
                            booklist.add(bookObject)
                           // dashboardRecycleAdapter= DashboardRecycleAdapter(activity as Context,booklist)
                          //  recyclerView.adapter= this.dashboardRecycleAdapter
                          //  recyclerView.layoutManager=layoutmanager

                        }
                        dashboardRecycleAdapter= DashboardRecycleAdapter(activity as Context,booklist)
                        recyclerView.adapter= dashboardRecycleAdapter
                        recyclerView.layoutManager=layoutmanager
                    }

                }
                catch (e:JSONException){
                    Toast.makeText(activity as Context,"some error  occured",Toast.LENGTH_LONG).show()
                }
                },

            Response.ErrorListener {
                Toast.makeText(activity as Context,"some error",Toast.LENGTH_LONG).show()


        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "8b46c31c0fb959"
                return headers
            }

        }

        queue.add(jsonObjectRequest)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       val id = item.itemId
        if (id==R.id.sort){
            Collections.sort(booklist,bookcompare)
            booklist.reverse()
        }
        dashboardRecycleAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

            }

