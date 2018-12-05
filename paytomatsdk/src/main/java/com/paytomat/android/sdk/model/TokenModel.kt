package com.paytomat.android.sdk.model

/**
 * created by Alex Ivanov on 2018-12-04.
 */
data class TokenModel(val amount: Double, val contract: String, val symbol: String, val precision: Int) {

    companion object {
        fun eos(amount: Double): TokenModel = TokenModel(amount, "eosio", "EOS", 4)
    }

    class Builder {
        private var amount: Double = 0.0
        private var contract: String = "eosio"
        private var symbol: String = "EOS"
        private var precision: Int = 4

        fun setAmount(amount: Double): Builder {
            this.amount = amount
            return this
        }

        fun setContract(contract: String): Builder {
            this.contract = contract
            return this
        }

        fun setSymbol(symbol: String): Builder {
            this.symbol = symbol
            return this
        }

        fun setPrecision(precision: Int): Builder {
            this.precision = precision
            return this
        }

        fun build(): TokenModel = TokenModel(amount, contract, symbol, precision)

    }

}