package com.paytomat.android.sdk

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.util.LruCache
import com.paytomat.android.sdk.model.*
import java.util.*

/**
 * created by Alex Ivanov on 11/29/18.
 */
object PaytomatSdk {

    private const val PACKAGE_NAME = "com.paytomat"
    private const val LOGIN_INTEGRATION_CLASS = "com.paytomat.ui.integration.authorization.AuthorizationActivity"
    private const val TRANSFER_INTEGRATION_CLASS = "com.paytomat.ui.integration.transfer.TransferActivity"

    private const val KEY_COMMON_UUID = "key.common.uuid"
    private const val KEY_COMMON_ERROR_CODE = "key.common.errorCode"
    private const val KEY_LOGIN_ACCOUNT_NAME = "key.login.accountName"
    private const val KEY_TRANSFER_TRANSACTION_ID = "key.transfer.transactionId"

    const val CODE_LOGIN_REQUEST: Int = 3937
    const val CODE_TRANSFER_REQUEST: Int = 5734


    private val lastActionsIds: LruCache<String, String> = LruCache(3)

    /**
     * Checks if Paytomat application is installed
     * @param context application or activity context
     * @return if Paytomat application is installed
     */
    @JvmStatic
    fun isPaytomatInstalled(context: Context): Boolean = try {
        context.packageManager.getApplicationInfo(PACKAGE_NAME, 0).enabled
    } catch (e: Throwable) {
        false
    }

    /**
     * Opens play store for Paytomat application
     * @param context activity context for intent handling
     */
    @JvmStatic
    fun requestDownload(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PACKAGE_NAME")))
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$PACKAGE_NAME"))
            )
        }
    }

    /**
     * Opens Paytomat integration login
     * @param activity activity which will be used for integration open
     * @param brandingModel info about your application
     * @param expireTimestamap timestamp when login action expire
     * @return bool if login integration was launched
     */
    @JvmStatic
    @JvmOverloads
    fun requestLogin(
        activity: Activity,
        brandingModel: BrandingModel,
        expireTimestamp: Long = System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS * 10
    ): Boolean {
        if (!isPaytomatInstalled(activity)) return false
        val uuid: String = UUID.randomUUID().toString()
        lastActionsIds.put(uuid, "login")
        val login: LoginModel = LoginModel.Builder()
            .setBranding(brandingModel)
            .setUUID(uuid)
            .setExpired(expireTimestamp)
            .build()
        activity.startActivityForResult(Intent().apply {
            setClassName(PACKAGE_NAME, LOGIN_INTEGRATION_CLASS)
            data = Uri.parse(login.toString())
        }, CODE_LOGIN_REQUEST)
        return true
    }

    /**
     * Handles back intent from Paytomat if intent is correct returns an account name
     * @param requestCode activity's request code
     * @param resultCode activity's result code
     * @param data data received from Paytomat side
     * @return account name if all data is accurate
     */
    @JvmStatic
    fun handleLoginResult(requestCode: Int, resultCode: Int, data: Intent?): Result<LoginAccount> {
        if (resultCode == RESULT_CANCELED && data == null) return Result(ERROR_CODE_CANCELED, "login", null)
        if (requestCode != CODE_LOGIN_REQUEST || data == null) return Result(ERROR_CODE_PARSE, "login", null)
        val uuid: String? = data.getStringExtra(KEY_COMMON_UUID)
        val accountName: String? = data.getStringExtra(KEY_LOGIN_ACCOUNT_NAME)
            ?.takeIf { uuid != null && lastActionsIds.get(uuid)?.equals("login") ?: false }
        var errorCode: Int =
            data.getIntExtra(KEY_COMMON_ERROR_CODE, SUCCESS).let { if (accountName != null) SUCCESS else it }

        if (errorCode !in arrayOf(
                SUCCESS, ERROR_CODE_NO_MNEMONIC, ERROR_CODE_NO_ACCOUNT, ERROR_CODE_INVALID_EOS_SYMBOL,
                ERROR_CODE_INVALID_RECIPIENT_ACCOUNT, ERROR_CODE_NOT_ENOUGHT_BALANCE,
                ERROR_CODE_PARSE, ERROR_CODE_UNKNOWN
            )
        ) {
            errorCode = ERROR_CODE_UNKNOWN
        }
        if (resultCode != Activity.RESULT_OK) return Result(errorCode, "login", null)
        val account = accountName?.let { LoginAccount(it) }
        return Result(errorCode, "login", account)
    }

    /**
     * Opens Paytomat integration transaction screen
     * @param activity activity which will be used for integration open
     * @param brandingModel info about your application
     * @param expireTimestamap timestamp when login action expire
     * @return bool if login integration was launched
     */
    @JvmStatic
    @JvmOverloads
    fun requestTransaction(
        activity: Activity,
        brandingModel: BrandingModel? = null,
        from: String? = null,
        to: String,
        tokenModel: TokenModel,
        memo: String? = null,
        expireTimestamp: Long = System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS * 10
    ): Boolean {
        if (!isPaytomatInstalled(activity)) return false
        val uuid: String = UUID.randomUUID().toString()
        lastActionsIds.put(uuid, "transfer")
        val transaction: TransactionModel = TransactionModel.Builder()
            .setBranding(brandingModel)
            .setFrom(from)
            .setTo(to)
            .setTokenModel(tokenModel)
            .setMemo(memo)
            .setUUID(uuid)
            .setExpired(expireTimestamp)
            .build()
        activity.startActivityForResult(Intent().apply {
            setClassName(PACKAGE_NAME, TRANSFER_INTEGRATION_CLASS)
            data = Uri.parse(transaction.toString())
        }, CODE_TRANSFER_REQUEST)
        return true
    }

    /**
     * Handles back intent from Paytomat if intent is correct returns an transaction ID
     * @param requestCode activity's request code
     * @param resultCode activity's result code
     * @param data data received from Paytomat side
     * @return transaction ID if all data is accurate
     */
    @JvmStatic
    fun handleTransferResult(requestCode: Int, resultCode: Int, data: Intent?): Result<TransferId> {
        if (resultCode == RESULT_CANCELED && data == null) return Result(ERROR_CODE_CANCELED, "transfer", null)
        if (requestCode != CODE_TRANSFER_REQUEST || data == null) return Result(ERROR_CODE_PARSE, "transfer", null)
        val uuid: String? = data.getStringExtra(KEY_COMMON_UUID)
        val transactionId: String? = data.getStringExtra(KEY_TRANSFER_TRANSACTION_ID)
        val transferId: String? =
            transactionId?.takeIf { uuid != null && lastActionsIds.get(uuid)?.equals("transfer") ?: false }
        var errorCode: Int =
            data.getIntExtra(KEY_COMMON_ERROR_CODE, SUCCESS).let {
                if (transferId != null) SUCCESS else it
            }
        if (errorCode !in arrayOf(
                SUCCESS, ERROR_CODE_NO_MNEMONIC, ERROR_CODE_NO_ACCOUNT, ERROR_CODE_INVALID_EOS_SYMBOL,
                ERROR_CODE_INVALID_RECIPIENT_ACCOUNT, ERROR_CODE_NOT_ENOUGHT_BALANCE,
                ERROR_CODE_PARSE, ERROR_CODE_CANCELED, ERROR_CODE_UNKNOWN
            )
        ) {
            errorCode = ERROR_CODE_UNKNOWN
        }
        if (resultCode != Activity.RESULT_OK) return Result(errorCode, "transfer", null)
        val transfer = transferId?.let { TransferId(it) }
        return Result(errorCode, "transfer", transfer)
    }
}