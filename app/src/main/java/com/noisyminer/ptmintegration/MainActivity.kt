package com.noisyminer.ptmintegration

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.paytomat.android.sdk.Codes
import com.paytomat.android.sdk.PaytomatSdk
import com.paytomat.android.sdk.model.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun triggerLogin(view: View) {
        val brandingModel: BrandingModel = BrandingModel.Builder()
            .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
            .setTitle("Integration Test")
            .setDescription("Login to PTM integration app")
            .build()
        if (!PaytomatSdk.requestLogin(this, brandingModel)) {
            Toast.makeText(applicationContext, " launch Intent not available", Toast.LENGTH_SHORT).show()
            PaytomatSdk.requestDownload(this)
        }
    }

    fun triggerTransfer(view: View) {
        val brandingModel: BrandingModel = BrandingModel.Builder()
            .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
            .setTitle("Integration Test")
            .setDescription("Login to PTM integration app")
            .build()
        val tokenModel: TokenModel = TokenModel.eos(0.001)
        if (!PaytomatSdk.requestTransaction(
                this,
                brandingModel = brandingModel,
                to = "someaddress1",
                tokenModel = tokenModel,
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
            val accountResult: Result<LoginAccount> = PaytomatSdk.handleLoginResult(requestCode, resultCode, data)
            if (accountResult.isSuccess()) accountResult.result?.also {
                Toast.makeText(
                    this,
                    it.accountName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else showError(accountResult.code)
            Log.d("<<SS", accountResult.toString())
        } else if (requestCode == PaytomatSdk.CODE_TRANSFER_REQUEST) {
            val transactionIdResult: Result<TransferId> =
                PaytomatSdk.handleTransferResult(requestCode, resultCode, data)
            if (transactionIdResult.isSuccess())
            Toast.makeText(
                this,
                transactionIdResult.result?.let { "Transaction ID is $it" } ?: "No transactionID",
                Toast.LENGTH_SHORT)
                .show()
            else showError(transactionIdResult.code)
            Log.d("<<SS", transactionIdResult.toString())
        }
    }

    private fun showError(errorCode: Int) {
        val errorRes: Int = when (errorCode) {
            Codes.SUCCESS -> return
            Codes.ERROR_CODE_NO_MNEMONIC -> R.string.error_no_mnemonic
            Codes.ERROR_CODE_NO_ACCOUNT -> R.string.error_no_account
            Codes.ERROR_CODE_INVALID_EOS_SYMBOL -> R.string.error_invalid_eos_symbol
            Codes.ERROR_CODE_INVALID_RECIPIENT_ACCOUNT -> R.string.error_invalid_recipient_address
            Codes.ERROR_CODE_NOT_ENOUGHT_BALANCE -> R.string.error_not_enough_balance
            Codes.ERROR_CODE_PARSE -> R.string.error_parse
            Codes.ERROR_CODE_CANCELED -> R.string.error_canceled
            else -> R.string.error_unknown
        }
        Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
    }
}