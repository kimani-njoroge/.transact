package com.example.transact

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.example.transact.database.DatabaseHandler
import com.example.transact.models.Transact
import kotlinx.android.synthetic.main.actvitiy_add_edit.*

class AddorEditActivity  : AppCompatActivity() {
    var dbHandler: DatabaseHandler? = null
    var isEditMode = false

    //call activity is first created
    public override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.actvitiy_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE //view is invisible but still takes up layout space
        if(intent != null && intent.getStringExtra("Mode") == "E") {//get data from intent
            isEditMode = true
            val transactions: Transact = dbHandler!!.getTransaction(intent.getIntExtra("Id", 0))
            //set tesxt to be displayed
            input_category.setText(transactions.category)
            input_status.setText(transactions.status)
            input_amount.setText(transactions.amount)
            input_info.setText(transactions.info)
            swt_completed.isChecked = transactions.completed == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener({
            var success: Boolean = false
            if (!isEditMode) {
                val transactions: Transact = Transact()
                transactions.category  = input_category.text.toString()
                transactions.status = input_status.text.toString()
                transactions.amount = input_amount.text.toString()
                transactions.info = input_info.text.toString()
                if (swt_completed.isChecked)
                    transactions.completed = "Y"
                else
                    transactions.completed = "N"
                success = dbHandler?.addTransaction(transactions) as Boolean
            } else {
                val transactions: Transact = Transact()
                transactions.id = intent.getIntExtra("Id", 0)
                transactions.category  = input_category.text.toString()
                transactions.status = input_status.text.toString()
                transactions.amount = input_amount.text.toString()
                transactions.info = input_info.text.toString()
                if (swt_completed.isChecked)
                    transactions.completed = "Y"
                else
                    transactions.completed = "N"
                success = dbHandler?.updateTransaction(transactions) as Boolean
            }

            if (success)
                finish()
        })

        btn_delete.setOnClickListener({
            //create alert dialog
            val dialog = AlertDialog.Builder(this).setTitle("Info")
                .setMessage("Click 'YES' Delete the Task.")//display message on alert
                .setPositiveButton("YES",
                    {dialog, i ->
                    val success = dbHandler?.deleteTransaction(intent.getIntExtra("Id",0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", {
                    dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
        })
    }

    //called when an item in the list is selected with
    //parameters item and result is boolean
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
            if (id == android.R.id.home) {
                finish()
                return true
            }
        return super.onOptionsItemSelected(item)
    }
}