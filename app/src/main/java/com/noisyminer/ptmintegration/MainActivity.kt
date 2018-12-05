package com.noisyminer.ptmintegration

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.paytomat.android.sdk.PaytomatSdk
import com.paytomat.android.sdk.model.BrandingModel
import com.paytomat.android.sdk.model.TokenModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun triggerLogin(view: View) {
        if (!PaytomatSdk.requestLogin(
                this,
                BrandingModel.Builder()
                    .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
                    .setTitle("Integration Test")
                    .setDescription("Login to PTM integration app")
                    .build()
            )
        ) {
            Toast.makeText(applicationContext, " launch Intent not available", Toast.LENGTH_SHORT).show()
            PaytomatSdk.requestDownload(this)
        }
    }

    fun triggerTransfer(view: View) {
        if (!PaytomatSdk.requestTransaction(
                this,
                brandingModel = BrandingModel.Builder()
                    .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
                    .setTitle("Integration Test")
                    .setDescription("Login to PTM integration app")
                    .build(),
                to = "co23sc5xj2kb",
                tokenModel = TokenModel.eos(0.001),
                memo = "PTM integration test"
            )
        ) {
            Toast.makeText(applicationContext, " launch Intent not available", Toast.LENGTH_SHORT).show()
            PaytomatSdk.requestDownload(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PaytomatSdk.CODE_LOGIN_REQUEST) {
            val accountName: String? = PaytomatSdk.handleLoginResult(requestCode, resultCode, data)
            Toast.makeText(this, accountName?.let { "Account name is $it" } ?: "No account name", Toast.LENGTH_SHORT)
                .show()
        } else if (requestCode == PaytomatSdk.CODE_TRANSFER_REQUEST) {
            val transactionId: String? = PaytomatSdk.handleTransferResult(requestCode, resultCode, data)
            Toast.makeText(
                this,
                transactionId?.let { "Transaction ID is $it" } ?: "No transactionID",
                Toast.LENGTH_SHORT)
                .show()
        }

    }
}