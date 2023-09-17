package com.example.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookstore.fragment.About
import com.example.bookstore.fragment.DashBoard
import com.example.bookstore.fragment.Favourites
import com.example.bookstore.fragment.Profile
import com.example.bookstore.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawer:DrawerLayout
    lateinit var toolbar:Toolbar
    lateinit var navigation:NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var coordinator:CoordinatorLayout
    var previousmenuitem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer=findViewById(R.id.drawer)
        toolbar=findViewById(R.id.toolbar)
        navigation=findViewById(R.id.navigationview)
        frameLayout=findViewById(R.id.framelayout)
        coordinator=findViewById(R.id.coordinatorlayout)
        openDashboard()

//to make every menu clickable

        navigation.setNavigationItemSelectedListener{
            if (previousmenuitem !=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem= it
        when(it.itemId){
            R.id.dashboard ->{
                openDashboard()
            }

            R.id.favourites ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, Favourites())
                    .commit()
                supportActionBar?.title="favourites"
                drawer.closeDrawers()

            }

            R.id.profile ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, Profile())
                    .commit()
                supportActionBar?.title="profile"
                drawer.closeDrawers()

            }

            R.id.about ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, About())
                    .commit()
                supportActionBar?.title="About"
                drawer.closeDrawers()

            }

        }
            return@setNavigationItemSelectedListener true
        }


        //calling the function
        setActiontoolbar()

        //to adding hamberger button
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawer,
            R.string.open_drawer,
            R.string.close_drawer
        )

        //to enable click on hamberger button
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



    }
//to add the title of toolbar
    fun setActiontoolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Dashboard"
    //to set menu button toolbar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
//to make hamberger button funtional
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawer.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    //to open the dashboard by default on launching app
    fun openDashboard(){
        val fragment = DashBoard()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout,fragment)
        transaction.commit()
        drawer.closeDrawers()
        navigation.setCheckedItem(R.id.dashboard)
        supportActionBar?.title="DashBoard"
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is DashBoard ->openDashboard()
            else->super.onBackPressed()

        }
    }


}
