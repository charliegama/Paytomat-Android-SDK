package com.paytomat.android.sdk.model

import com.paytomat.android.sdk.Codes.SUCCESS

/**
 * created by Alex Ivanov on 2018-12-06.
 */
data class Result<T>(val code: Int, val type: String, val result: T?) {

    fun isSuccess(): Boolean = code == SUCCESS && result != null
}

data class LoginAccount(val accountName: String)

data class TransferId(val transactionId: String)