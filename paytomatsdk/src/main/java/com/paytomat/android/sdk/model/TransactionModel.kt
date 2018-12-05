package com.paytomat.android.sdk.model

import android.text.format.DateUtils
import org.json.JSONObject
import java.util.*

/**
 * created by Alex Ivanov on 2018-12-04.
 */
data class TransactionModel(
    val protocol: String,
    val version: String,
    val name: String?,
    val iconUrl: String?,
    val action: String,
    var from: String?,
    val to: String,
    val amount: Double,
    val contract: String,
    val symbol: String,
    val precision: Int,
    val dappData: String?,
    val desc: String?,
    val expired: Long,
    val callback: String?
) {

    class Builder {
        private var brandingModel: BrandingModel? = null
        private var from: String? = null
        private lateinit var to: String
        private lateinit var tokenModel: TokenModel
        private var memo: String? = null
        private var expired: Long = 0
        private var uuID: String? = null

        fun setBranding(branding: BrandingModel?): Builder {
            this.brandingModel = branding
            return this
        }

        fun setFrom(from: String?): Builder {
            this.from = from
            return this
        }

        fun setTo(to: String): Builder {
            this.to = to
            return this
        }

        fun setTokenModel(tokenModel: TokenModel): Builder {
            this.tokenModel = tokenModel
            return this
        }

        fun setMemo(memo: String?): Builder {
            this.memo = memo
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

        fun build(): TransactionModel = TransactionModel(
            protocol = "PaytomatIntegration",
            version = "1.0",
            name = brandingModel?.title,
            iconUrl = brandingModel?.iconUrl,
            desc = brandingModel?.description,
            action = "transfer",
            from = from,
            to = to,
            amount = tokenModel.amount,
            contract = tokenModel.contract,
            symbol = tokenModel.symbol,
            precision = tokenModel.precision,
            dappData = memo,
            expired = if (expired > System.currentTimeMillis()) expired else System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS * 10,
            callback = uuID ?: UUID.randomUUID().toString()
        )
    }

    override fun toString(): String {
        val json = JSONObject()
        json.put("protocol", protocol)
        json.put("version", version)
        json.put("dappName", name)
        json.put("dappIcon", iconUrl)
        json.put("action", action)
        json.put("from", from)
        json.put("to", to)
        json.put("amount", amount)
        json.put("contract", contract)
        json.put("symbol", symbol)
        json.put("precision", precision)
        json.put("dappData", dappData)
        json.put("desc", desc)
        json.put("expired", expired)
        json.put("callback", callback)
        return json.toString()
    }
}