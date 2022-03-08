package com.moneyhash.moneyhash.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.moneyhash.sdk.android.MoneyHash
import com.moneyhash.sdk.android.PaymentResultContract
import com.moneyhash.sdk.android.PaymentStatus

class MainActivity : AppCompatActivity() {

    lateinit var statusTextview: TextView
    lateinit var idEditText: EditText
    lateinit var actionButton: Button

    private val paymentResultContract =
        registerForActivityResult(PaymentResultContract()) { result ->
            if(result != null) {
                when(result){
                    is PaymentStatus.Error -> {
                        statusTextview.text = result.errors.joinToString()
                    }
                    is PaymentStatus.Failed -> {
                        statusTextview.text = "Failed\n${result.result}"
                    }
                    is PaymentStatus.RequireExtraAction -> {
                        statusTextview.text = "RequireExtraAction\n${result.actions.joinToString()}\n${result.result}"
                    }
                    is PaymentStatus.Success -> {
                        statusTextview.text = "Success\n${result.result}"
                    }

                    is PaymentStatus.Unknown -> {
                        statusTextview.text = "Unknown"
                    }
                    is PaymentStatus.Cancelled -> {
                        statusTextview.text = "Cancelled"
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextview = findViewById(R.id.status_textview)
        idEditText = findViewById(R.id.id_edittext)
        actionButton = findViewById(R.id.action_button)

        actionButton.setOnClickListener {
            val paymentIntentId = idEditText.text.toString()
            if(paymentIntentId.isEmpty()){
                Toast.makeText(this, "Please enter valid payment intent id", Toast.LENGTH_LONG).show()
            } else {
                MoneyHash.INSTANCE.start(paymentIntentId, paymentResultContract)
            }
        }
    }
}