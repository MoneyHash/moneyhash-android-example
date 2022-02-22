package com.moneyhash.moneyhash.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.moneyhash.sdk.android.MoneyHash
import com.moneyhash.sdk.android.PaymentResultContract
import com.moneyhash.sdk.android.PaymentStatus

class MainActivity : AppCompatActivity() {

    lateinit var tv: TextView

    private val paymentResultContract =
        registerForActivityResult(PaymentResultContract()) { result ->
            if(result != null) {
                when(result){
                    is PaymentStatus.Error -> {
                        tv.text = result.errors.joinToString()
                    }
                    is PaymentStatus.Failed -> {
                        tv.text = "Failed"
                    }
                    is PaymentStatus.RequireExtraAction -> {
                        tv.text = result.actions.joinToString()
                    }
                    is PaymentStatus.Success -> {
                        tv.text = "Success"
                    }
                    is PaymentStatus.Unknown -> {
                        tv.text = "Unknown"
                    }
//                    is PaymentStatus.Cancelled -> {
//                        tv.text = "Cancelled"
//                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.text_view)
        tv.text = "Click Here to start Payment Flow"
        val paymentId = "39R2Y9N"

        tv.setOnClickListener {
            MoneyHash.INSTANCE.start(paymentId, paymentResultContract)
        }
    }
}