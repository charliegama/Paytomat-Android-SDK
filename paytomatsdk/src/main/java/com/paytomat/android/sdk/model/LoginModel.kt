package com.paytomat.android.sdk.model

import android.text.format.DateUtils
import org.json.JSONObject
import java.util.*

/**
 * created by Alex Ivanov on 11/29/18.
 */
internal data class LoginModel(
    val protocol: String,
    val version: String,
    val name: String,
    val iconUrl: String,
    val action: String,
    val uuID: String,
    val loginUrl: String,
    val expired: Long,
    val loginMemo: String?
) {

    class Builder {
        private var brandingModel: BrandingModel? = null
        private var uuID: String? = null
        private var expired: Long = 0

        fun setBranding(branding: BrandingModel): Builder {
            this.brandingModel = branding
            return this
        }

        fun setUUID(uuid: String): Builder {
            this.uuID = uuid
            return this
        }

        fun setExpired(expired: Long): Builder {
            this.expired = expired
            return this
        }

        fun build(): LoginModel = LoginModel(
            protocol = "PaytomatIntegration",
            version = "1.0",
            name = brandingModel?.title ?: "",
            iconUrl = brandingModel?.iconUrl ?: "",
            loginMemo = brandingModel?.description ?: "",
            action = "login",
            uuID = uuID ?: UUID.randomUUID().toString(),
            loginUrl = "",
            expired = if (expired > System.currentTimeMillis()) expired else System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS * 10
        )
    }

    override fun toString(): String {
        val json = JSONObject()
        json.put("protocol", protocol)
        json.put("version", version)
        json.put("dappName", name)
        json.put("dappIcon", iconUrl)
        json.put("action", action)
        json.put("uuID", uuID)
        json.put("loginUrl", loginUrl)
        json.put("expired", expired)
        json.put("loginMemo", loginMemo)
        return json.toString()
    }
}