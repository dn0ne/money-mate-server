package com.dn0ne.moneymateserver.models.spending

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

/**
 * Category class
 * @property id Id of the category (provided automatically)
 * @property name Category name (by default - empty string)
 * @property iconName Category icon name, taken from [CategoryIcons] (by default - empty string)
 */
@Serializable
data class Category (
    @Contextual @Id var id: ObjectId = ObjectId(),
    var name: String = "",
    var iconName: String = ""
)