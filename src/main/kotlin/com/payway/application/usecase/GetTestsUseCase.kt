package com.payway.application.usecase

import com.payway.application.port.`in`.GetTestsQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GetTestsUseCase: GetTestsQuery {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun execute(): String {
        log.info("Returning tests")
        return "Het I got it!"
    }
}