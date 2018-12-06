package com.paytomat.android.sdk

/**
 * created by Alex Ivanov on 2018-12-06.
 */
//SUCCESS
const val SUCCESS = -1
// Wallet seed phrase is not entered
const val ERROR_CODE_NO_MNEMONIC = 0
// EOS Account is not created by the wallet
const val ERROR_CODE_NO_ACCOUNT = 1
// EOS Symbol encoding failed
const val ERROR_CODE_INVALID_EOS_SYMBOL = 2
// EOS Recipient account is not encodable
const val ERROR_CODE_INVALID_RECIPIENT_ACCOUNT = 3
//Insufficient token balance
const val ERROR_CODE_NOT_ENOUGHT_BALANCE = 4
//Unable to parse model
const val ERROR_CODE_PARSE = 5
//User canceled login action
const val ERROR_CODE_CANCELED = 6
//Unknown error from application
const val ERROR_CODE_UNKNOWN = Int.MAX_VALUE