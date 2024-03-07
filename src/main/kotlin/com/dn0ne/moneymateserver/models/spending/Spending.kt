package com.dn0ne.moneymateserver.models.spending

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

/**
 * Spending class
 * @property id Spending Id (provided automatically)
 * @property category Spending category (by default - null)
 * @property amount Spending amount
 * @property shortDescription Short description of spending
 * @property shoppingList RealmList of shopping items
 * @property spentAt Spending date
 */
@Serializable
data class Spending(
    @Contextual @Id var id: ObjectId = ObjectId(),
    var category: Category? = null,
    var amount: Float = .0f,
    var shortDescription: String? = null,
    var shoppingList: List<ShoppingItem> = listOf(),
    var spentAt: Instant = Clock.System.now()
)