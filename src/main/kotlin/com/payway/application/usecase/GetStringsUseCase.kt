package com.payway.application.usecase

import com.payway.application.port.`in`.GetStringsQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GetStringsUseCase: GetStringsQuery {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun execute(): List<String> {
        log.info("Returning strings")
        return listOf("Het I got it!")
    }
}