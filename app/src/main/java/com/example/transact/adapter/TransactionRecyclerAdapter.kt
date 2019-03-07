package com.example.transact.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.transact.AddorEditActivity
import com.example.transact.R
import com.example.transact.models.Transact

class TransactionRecyclerAdapter(transactionList: List<Transact>,
        internal var context: Context) :
        RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder>() {
    internal var transactionList: List<Transact> = ArrayList()
    init {
        this.transactionList = transactionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : TransactionViewHolder {
        //create viewholder and initiallize fields(category,amount,info and status)
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_transactions, parent, false)
        //inflate to instantiate xml layout to view
        return TransactionViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)//specify from which API level to target
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        //update the viewholder contents abd set them up to be used by used by the
        //recyclerview
        val transactions = transactionList[position]
        holder.category.text = transactions.category
        holder.status.text = transactions.status
        holder.amount.text = transactions.amount
        holder.info.text = transactions.info

        if (transactions.completed == "Y")
            //check whether the result is Y
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorSuccess)
        else
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)
        holder.itemView.setOnClickListener {
            //set action to commence the activity
            val i = Intent(context, AddorEditActivity::class.java)// launch activity
            i.putExtra("Mode", "E")//add extended data to the intent
            i.putExtra("Id",transactions.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK //avoid restarting a task that already exists
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        //return total no of items in the data set
        return transactionList.size
    }

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var category: TextView = view.findViewById(R.id.tvCategory) as TextView
        var status: TextView = view.findViewById(R.id.tvStatus) as TextView
        var amount: TextView = view.findViewById(R.id.tvAmount) as TextView
        var info: TextView = view.findViewById(R.id.tvInfo) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
    }
}