package com.dn0ne.moneymateserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class MoneyMateServerApplication {
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World"
    }
}

fun main(args: Array<String>) {
    runApplication<MoneyMateServerApplication>(*args)
}
