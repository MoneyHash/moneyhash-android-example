package com.moneyhash.moneyhash.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.moneyhash.sdk.android.common.IntentType
import com.moneyhash.sdk.android.core.IntentContract
import com.moneyhash.sdk.android.core.MoneyHashSDKBuilder
import com.moneyhash.sdk.android.result.ResultType

class MainActivity : AppCompatActivity() {

    lateinit var statusTextview: TextView
    lateinit var idEditText: EditText
    lateinit var actionButton: Button

    private val resultContract =
        registerForActivityResult(IntentContract()) { result ->

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
                MoneyHashSDKBuilder.build()
                    .renderForm(
                        intentId = idEditText.text.toString(),
                        intentType = IntentType.Payment,
                        launcher = resultContract,
                        resultType = ResultType.RESULT_SCREEN_WITH_CALLBACK // Result type can be RESULT_SCREEN_WITH_CALLBACK or CALLBACK (to not render moneyhash success screen)
                    )
            }
        }
    }
}