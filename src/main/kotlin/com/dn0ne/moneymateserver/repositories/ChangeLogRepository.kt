package com.dn0ne.moneymateserver.repositories

import com.dn0ne.moneymateserver.models.change.ChangeLog
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ChangeLogRepository: MongoRepository<ChangeLog, ObjectId> {
    fun findChangeLogByUid(uid: ObjectId): ChangeLog?
}