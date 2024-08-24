package com.example.expensetracker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class expenseadapter (private val context: Context, expenseModelArrayList: ArrayList<expensemodel>) :
        RecyclerView.Adapter<expenseadapter.Viewholder>(){
            private val expenseModelArrayList: ArrayList<expensemodel>
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): expenseadapter.Viewholder {
            val view: View= LayoutInflater.from(parent.context).inflate(R.layout.cardview_for_expenses,parent,false)
            return Viewholder(view)
    }

    override fun onBindViewHolder( holder: expenseadapter.Viewholder, position: Int) {
        val model: expensemodel= expenseModelArrayList[position]
        holder.expenseprice.text= model.getexpenseprice()
        holder.expensedate.text= model.getexpensedate()
        if (model.isCredit()==1) {
            holder.cardView.setCardBackgroundColor(Color.GREEN) // Change to your desired color
        } else {
            holder.cardView.setCardBackgroundColor(Color.RED) // Change to your desired color
        }
    }

    override fun getItemCount(): Int {
        return expenseModelArrayList.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseprice: TextView = itemView.findViewById(R.id.expenseprice)
        val expensedate: TextView = itemView.findViewById(R.id.expensedate)
        val cardView: CardView= itemView.findViewById(R.id.expensecard)
    }

    init{
        this.expenseModelArrayList=expenseModelArrayList
    }

}