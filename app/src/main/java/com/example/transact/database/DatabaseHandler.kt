package com.example.transact.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.transact.models.Transact

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context,
    DatabaseHandler.DB_NAME,null, DatabaseHandler.DB_VERSION) {
    //create database and corresponding fields
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$CATEGORY TEXT,$STATUS TEXT,$AMOUNT TEXT,$INFO TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    //add transaction
    fun addTransaction(transactions: Transact) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CATEGORY, transactions.category)
        values.put(STATUS, transactions.status)
        values.put(AMOUNT, transactions.amount)
        values.put(INFO, transactions.info)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    //get transaction
    fun getTransaction(_id: Int) : Transact{
        val transactions = Transact()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        transactions.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        transactions.category = cursor.getString(cursor.getColumnIndex(CATEGORY))
        transactions.status = cursor.getString(cursor.getColumnIndex(STATUS))
        transactions.amount = cursor.getString(cursor.getColumnIndex(AMOUNT))
        transactions.info = cursor.getString(cursor.getColumnIndex(INFO))
        cursor.close()
        return transactions
    }

    //list all transactions
    fun transacts() : List<Transact> {
        val transactionList = ArrayList<Transact>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null){
            if(cursor.moveToFirst()) {
                do {
                    val transactions = Transact()
                    transactions.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    transactions.category = cursor.getString(cursor.getColumnIndex(CATEGORY))
                    transactions.status = cursor.getString(cursor.getColumnIndex(STATUS))
                    transactions.amount = cursor.getString(cursor.getColumnIndex(AMOUNT))
                    transactions.info = cursor.getString(cursor.getColumnIndex(INFO))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return transactionList
    }

    //update transaction
    fun updateTransaction(transactions: Transact) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CATEGORY, transactions.category)
        values.put(STATUS, transactions.status)
        values.put(AMOUNT, transactions.amount)
        values.put(INFO, transactions.info)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(transactions.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    //delete all Transactions
    fun deleteAllTransactions() : Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("_success") != -1
    }

    //delete single transaction

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "MyTransactions"
        private val TABLE_NAME = "Transactions"
        private val ID = "Id"
        private val CATEGORY = "category"
        private val STATUS = "status"
        private val AMOUNT = "amount"
        private val INFO = "info"


    }
}
