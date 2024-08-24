package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.databinding.FragmentBankChangeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Bank_change : BottomSheetDialogFragment(){
    lateinit var binding: FragmentBankChangeBinding
    private lateinit var bankchangemodel: BankChangeModel

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding= FragmentBankChangeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        val activity = requireActivity()
        bankchangemodel = ViewModelProvider(activity).get(BankChangeModel::class.java)
        binding.btnfindexpenses.setOnClickListener {
            findexpense()
        }
    }

    private fun findexpense() {
        bankchangemodel.bankname.value=binding.bankname.text.toString()
        bankchangemodel.currency.value=binding.currencyname.text.toString()
        Log.i("Maindata", bankchangemodel.bankname.value ?:"no name")
        Log.i("Maindata", bankchangemodel.currency.value ?:"no currency")
        binding.bankname.setText("")
        binding.currencyname.setText("")
        dismiss()

    }
}
