package com.poly.mybookapp.uibook
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.poly.mybookapp.R
import com.poly.mybookapp.adapter.MyBookAdapter
import com.poly.mybookapp.databinding.ActivityMainBinding
import com.poly.mybookapp.event.UpdateCartEvent
import com.poly.mybookapp.listener.BookLoadListener
import com.poly.mybookapp.listener.ICartLoadListener
import com.poly.mybookapp.model.BookModel
import com.poly.mybookapp.model.CartModel
import com.poly.mybookapp.ui.Profile
import com.poly.mybookapp.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() , BookLoadListener, ICartLoadListener{
    private lateinit var binding: ActivityMainBinding
    lateinit var bookLoadListener: BookLoadListener
    lateinit var cartLoadListener: ICartLoadListener
    lateinit var toggle: ActionBarDrawerToggle


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent){
        cartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext, "Home", Toast.LENGTH_LONG).show()
                R.id.nav_profile->{
                    var intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                }
                R.id.nav_cart -> Toast.makeText(applicationContext, "Cart", Toast.LENGTH_LONG).show()
                R.id.nav_logout -> Toast.makeText(applicationContext, "Logout", Toast.LENGTH_LONG).show()
            }
            true
        }


//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        binding.buttonCart.setOnClickListener {
            startActivity(Intent(this, Cart::class.java)) }
        init()

        loadBookFromFirebase()

        cartFromFirebase()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun cartFromFirebase(){
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel :: class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadBookFromFirebase(){

        // sau 2 giây thì hiển thị list book
        GlobalScope.launch { Dispatchers.IO
            delay(2000)
            withContext(Dispatchers.Main){

            }
            val bookModels: MutableList<BookModel> = ArrayList()
            FirebaseDatabase.getInstance().getReference("Books")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (bookSnapshot in snapshot.children) {
                                val bookModel = bookSnapshot.getValue(BookModel::class.java)
                                bookModel!!.key = bookSnapshot.key
                                bookModels.add(bookModel)
                            }
                            bookLoadListener.onBookLoadSuccess(bookModels)
                        } else {
                            bookLoadListener.onBookLoadFailed("Book items not exits")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        bookLoadListener.onBookLoadFailed(error.message)
                    }

                })

    }
}

    private fun init(){
        bookLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this,1)
        rcv.layoutManager = gridLayoutManager
        rcv.addItemDecoration(SpaceItemDecoration())

    }

    override fun onBookLoadSuccess(bookModelList: List<BookModel>) {
        val adapter = MyBookAdapter(this, bookModelList!!, cartLoadListener)
        rcv.adapter = adapter
    }

    override fun onBookLoadFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) cartSum += cartModel!!.quantity
        badge!!.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout, message!!,Snackbar.LENGTH_LONG).show()
    }
}