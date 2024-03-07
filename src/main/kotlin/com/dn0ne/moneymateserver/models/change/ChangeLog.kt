package com.dn0ne.moneymateserver.models.change

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("change_logs")
data class ChangeLog(
    @Id val uid: ObjectId = ObjectId(),
    val changeList: List<Change> = listOf()
)