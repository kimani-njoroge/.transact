package com.example.transact

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.transact.adapter.TransactionRecyclerAdapter
import com.example.transact.database.DatabaseHandler
import com.example.transact.models.Transact
import android.support.design.widget.FloatingActionButton


class MainActivity : AppCompatActivity() {

    var transactionRecyclerAdapter: TransactionRecyclerAdapter? = null
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var listTransactions: List<Transact> = ArrayList<Transact>()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//        setTheme(R.style.AppTheme)
//        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        listTransactions = (dbHandler as DatabaseHandler).transaction()
        transactionRecyclerAdapter = TransactionRecyclerAdapter(transactionList = listTransactions, context = applicationContext)
        (recyclerView as RecyclerView).adapter = transactionRecyclerAdapter
    }

    fun initViews() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab) as FloatingActionButton
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        transactionRecyclerAdapter = TransactionRecyclerAdapter(transactionList = listTransactions, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations(){
        fab?.setOnClickListener { view ->
            val i = Intent(applicationContext,AddorEditActivity::class.java)
            i.putExtra("Mode","A")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info")
                .setMessage("Tap 'YES' to delete all transactions")
                .setPositiveButton("YES",{ dialog, i ->
                    dbHandler!!.deleteAllTransactions()
                    initDB()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", {
                    dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }
}

