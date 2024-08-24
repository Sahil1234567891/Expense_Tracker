package com.example.expensetracker

class expensemodel (private var expenseprice: String, private var expensedate: String, var Credit: Int) {
    fun getexpenseprice():String{
        val expensepricecomplete="Rs. $expenseprice"
        return expensepricecomplete
    }
    fun setexpenseprice(expenseprice: String){
        this.expenseprice = expenseprice
    }
    fun getexpensedate():String{
        return expensedate
    }
    fun setexpensedate(expensedate: String){
        this.expensedate=expensedate
    }
    fun isCredit():Int{
        return Credit
    }
}