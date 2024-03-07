package com.dn0ne.moneymateserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoneyMateServerApplication

fun main(args: Array<String>) {
    runApplication<MoneyMateServerApplication>(*args)
}
