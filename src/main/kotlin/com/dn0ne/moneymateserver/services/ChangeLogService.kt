package com.dn0ne.moneymateserver.services

import com.dn0ne.moneymateserver.models.change.Change
import com.dn0ne.moneymateserver.models.change.ChangeLog
import com.dn0ne.moneymateserver.repositories.ChangeLogRepository
import com.dn0ne.moneymateserver.repositories.UserRepository
import com.dn0ne.moneymateserver.services.exceptions.NotFoundException
import com.dn0ne.moneymateserver.utils.JwtHelper
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChangeLogService(
    @Autowired private val changeLogRepository: ChangeLogRepository,
    @Autowired private val userRepository: UserRepository
) {

    private fun loadChanges(token: String): List<Change> {
        val email = JwtHelper.extractUsername(token)
        val user = userRepository.findUserByEmail(email)
            ?: throw NotFoundException("User does not exist, email: $email")

        val changeLog = changeLogRepository.findChangeLogByUid(user.id)
            ?: throw NotFoundException("Change log does not exist for email: $email")

        return changeLog.changeList
    }

    fun getChanges(token: String): List<Change> {
        return loadChanges(token).optimizeChanges()
    }

    fun getChangesAfterId(token: String, changeId: String): List<Change> {
        val changes = loadChanges(token)

        val targetChangeId = ObjectId(changeId)
        return changes.dropWhile {
            it.changeId != targetChangeId
        }.drop(1).optimizeChanges()
    }

    fun insertChanges(token: String, changes: List<Change>) {
        val email = JwtHelper.extractUsername(token)
        val user = userRepository.findUserByEmail(email)
            ?: throw NotFoundException("User does not exist, email: $email")

        val changeLog = changeLogRepository.findChangeLogByUid(user.id)
            ?: ChangeLog(uid = user.id)

        val updatedChangeLog = changeLog.copy(
            changeList = changeLog.changeList + changes
        )

        changeLogRepository.save(updatedChangeLog)
    }
}

fun List<Change>.optimizeChanges(): List<Change> {
    val listOfDeletedSpendingId = filterIsInstance<Change.DeleteSpending>().map { it.documentId }
    val listOfInsertedSpendingId = filterIsInstance<Change.InsertSpending>().map { it.document.id }

    val listOfDeletedCategoryId = filterIsInstance<Change.DeleteCategory>().map { it.documentId }
    val listOfInsertedCategoryId = filterIsInstance<Change.InsertCategory>().map { it.document.id }

    val optimizedList = filter { change ->
        when(change) {
            is Change.InsertCategory -> change.document.id !in listOfDeletedCategoryId
            is Change.UpdateCategory -> change.document.id !in listOfDeletedCategoryId
            is Change.DeleteCategory -> change.documentId !in listOfInsertedCategoryId

            is Change.InsertSpending -> change.document.id !in listOfDeletedSpendingId
            is Change.UpdateSpending -> change.document.id !in listOfDeletedSpendingId
            is Change.DeleteSpending -> change.documentId !in listOfInsertedSpendingId
        }
    }

    return optimizedList
}