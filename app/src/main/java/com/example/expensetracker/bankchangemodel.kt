package com.example.expensetracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BankChangeModel: ViewModel (){
    var bankname= MutableLiveData<String>().apply { value = "COSMOS" }
    var currency= MutableLiveData<String>().apply { value = "INR." }
}