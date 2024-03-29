package com.dn0ne.moneymateserver.controllers

import com.dn0ne.moneymateserver.models.change.Change
import com.dn0ne.moneymateserver.services.ChangeLogService
import com.dn0ne.moneymateserver.services.exceptions.NotFoundException
import com.dn0ne.moneymateserver.utils.JsonParser
import com.dn0ne.moneymateserver.utils.JwtHelper
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/changelog")
class ChangeLogController(
    @Autowired private val changeLogService: ChangeLogService
) {
    private val logger = LoggerFactory.getLogger(ChangeLogController::class.java)

    @GetMapping("/all")
    fun getChanges(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String
    ): ResponseEntity<List<Change>> {
        val token = JwtHelper.extractTokenFromHeader(authHeader)

        return try {
            val changes = changeLogService.getChanges(token)
            ResponseEntity.ok(changes)
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/afterId")
    fun getChangesAfterId(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody changeId: String
    ): ResponseEntity<List<Change>> {
        val token = JwtHelper.extractTokenFromHeader(authHeader)

        return try {
            val changesAfterId = changeLogService.getChangesAfterId(token, changeId)
            ResponseEntity.ok(changesAfterId)
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.unprocessableEntity().build()
        }
    }

    @PatchMapping("/insert", consumes = ["application/json"])
    fun insertChanges(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody changesString: String
    ): ResponseEntity<String> {
        val token = JwtHelper.extractTokenFromHeader(authHeader)

        return try {
            val changes = JsonParser.json.decodeFromString<List<Change>>(changesString)
            changeLogService.insertChanges(token, changes)
            ResponseEntity.ok().build()
        } catch (e: SerializationException) {
            ResponseEntity.badRequest().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }
}