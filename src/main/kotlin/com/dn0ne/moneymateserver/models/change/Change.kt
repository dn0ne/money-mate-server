package com.dn0ne.moneymateserver.models.change

import com.dn0ne.moneymateserver.models.spending.Category
import com.dn0ne.moneymateserver.models.spending.Spending
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
sealed class Change {
    @Contextual
    abstract val changeId: ObjectId

    @Serializable
    @SerialName("Change.InsertSpending")
    data class InsertSpending(
        @Contextual override val changeId: ObjectId,
        val document: Spending
    ) : Change()

    @Serializable
    @SerialName("Change.UpdateSpending")
    data class UpdateSpending(
        @Contextual override val changeId: ObjectId,
        val document: Spending
    ) : Change()

    @Serializable
    @SerialName("Change.DeleteSpending")
    data class DeleteSpending(
        @Contextual override val changeId: ObjectId,
        @Contextual val documentId: ObjectId
    ) : Change()

    @Serializable
    @SerialName("Change.InsertCategory")
    data class InsertCategory(
        @Contextual override val changeId: ObjectId,
        val document: Category
    ) : Change()

    @Serializable
    @SerialName("Change.UpdateCategory")
    data class UpdateCategory(
        @Contextual override val changeId: ObjectId,
        val document: Category
    ) : Change()

    @Serializable
    @SerialName("Change.DeleteCategory")
    data class DeleteCategory(
        @Contextual override val changeId: ObjectId,
        @Contextual val documentId: ObjectId
    ) : Change()
}