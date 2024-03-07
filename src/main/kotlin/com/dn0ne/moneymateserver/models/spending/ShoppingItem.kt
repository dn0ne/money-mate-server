package com.dn0ne.moneymateserver.models.spending

import kotlinx.serialization.Serializable

/**
 * Shopping item class
 * @property name Item name
 * @property price Item price
 */
@Serializable
data class ShoppingItem(
    var name: String = "",
    var price: Float = .0f
)