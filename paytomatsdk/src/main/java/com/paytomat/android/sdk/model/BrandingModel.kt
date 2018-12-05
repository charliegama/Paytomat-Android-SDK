package com.paytomat.android.sdk.model

/**
 * created by Alex Ivanov on 11/29/18.
 */
 data class BrandingModel(
    val iconUrl: String,
    val title: String,
    val description: String
) {

    class Builder {
        private var iconUrl: String? = null
        private var title: String? = null
        private var description: String? = null

        fun setIconUrl(iconUrl: String): Builder {
            this.iconUrl = iconUrl
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun build(): BrandingModel = BrandingModel(iconUrl ?: "", title ?: "", description ?: "")
    }
}