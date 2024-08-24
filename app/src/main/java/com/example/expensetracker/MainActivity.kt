package com.example.expensetracker

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bankchangemodel: BankChangeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bankchangemodel = ViewModelProvider(this).get(BankChangeModel::class.java)

        var smsList = ArrayList<String>()
        var dateList = ArrayList<String>()
        var creditornot= ArrayList<Int>()
        val READ_SMS_PERMISSION_CODE=1
        var bankname: String? =bankchangemodel.bankname.value.toString()
        var bankcurrency:String=bankchangemodel.currency.value.toString()



        fun extractexpense(message: String, bankcurrency: String): String {
            val regex = Regex("""\b$bankcurrency \s*(\d+)""")
            val matchResult = regex.find(message)
            return matchResult?.groupValues?.get(1) ?: ""
        }
        fun extractdate(message: String, hyphen:Int=0): String {
            var regex = Regex("""\bon\s*(\d+/\d+/\d+)""")
            if(hyphen!=0){
                regex = Regex("""\bon\s*(\d+-\d+-\d+)""")
            }
            val matchResult = regex.find(message)
            return matchResult?.groupValues?.get(1) ?: ""
        }

        fun readSms(senderPhoneNumber: String) {
            val contentResolver: ContentResolver = contentResolver

            // Define projection (columns to retrieve)
            val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY)

            // Define selection (where clause)
            val selection = "${Telephony.Sms.ADDRESS} LIKE ?"

            // Define selection arguments
            val selectionArgs = arrayOf("%$senderPhoneNumber%")

            // Query SMS messages with filter
            val cursor: Cursor? = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val address: String = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body: String = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    var extractexpenses = extractexpense(body,bankcurrency)
                    var extractdate = extractdate(body)
                    if("credited" in body){
                        creditornot.add(1)
                        bankcurrency="INR"
                        extractexpenses = extractexpense(body,bankcurrency)
                        extractdate=extractdate(body,1)
                    }
                    if("debited" in body){
                        creditornot.add(0)
                    }
                    if(extractexpenses != ""){
                        smsList.add("$extractexpenses")
                    }
                    if(extractdate!= ""){
                        dateList.add("$extractdate")
                    }
                }
            }

            cursor?.close()
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_SMS), READ_SMS_PERMISSION_CODE)
        } else {
            readSms("$bankname")
        }

            val expenserecycler = findViewById<RecyclerView>(R.id.expenserecycler)

        val expenseModelArrayList: ArrayList<expensemodel> = ArrayList()

        var datecounter=0
        Log.i("Maindata", smsList.toString())
        Log.i("Maindata", dateList.toString())
        Log.i("Maindata", creditornot.toString())
        for (i in smsList){
            val date:String= dateList.get(datecounter)
            val isCredit: Int = creditornot[datecounter]
            datecounter=datecounter+1
            val price: String = i
            expenseModelArrayList.add(expensemodel(price,date,isCredit))
        }

        expenseModelArrayList.add(expensemodel("50","1 jul 24",0))


        val expenseadapter = expenseadapter(this, expenseModelArrayList)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        expenserecycler.layoutManager = linearLayoutManager
        expenserecycler.adapter = expenseadapter

        binding.btnchangebank.setOnClickListener {
            val bankChangeFragment = Bank_change()
            bankChangeFragment.show(supportFragmentManager, "change bank")
        }
        bankchangemodel.bankname.observe(this){
            if(bankchangemodel.bankname.value!=bankname){
                bankchangemodel.currency.observe(this){
                    if(bankchangemodel.currency.value!=bankcurrency){
                        bankcurrency= bankchangemodel.currency.value!!
                    }
                    Log.i("Maindata","inner new $bankcurrency")
                }
                bankname=bankchangemodel.bankname.value
                smsList.clear()
                dateList.clear()
                creditornot.clear()
                readSms(bankname.toString())
            }
            Log.i("Maindata","inner new $bankname")
        }
    }
}