package com.payway.adapter.`in`.controller

import com.payway.adapter.utils.RoleConstants
import com.payway.adapter.utils.Security
import com.payway.application.port.`in`.GetTestsQuery
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tests")
@EnableGlobalMethodSecurity(prePostEnabled = true)
class TestController(
    private val getTestsQuery: GetTestsQuery
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PreAuthorize("hasAuthority('${RoleConstants.ADMIN_ROLE}')")
    @GetMapping("/admin")
    fun getAdmin(): ResponseEntity<String> {
        val siteId = Security.getSiteId()
        log.info("Getting tests for siteId: $siteId with admin role")
        val tests = getTestsQuery.execute()
        log.info(tests)
        return ResponseEntity.ok("¡La autenticación con admin ha sido exitosa!")
    }

    @PreAuthorize("hasAuthority('${RoleConstants.INTEGRATIONS_ROLE}')")
    @GetMapping("/integrations")
    fun getIntegrations(): ResponseEntity<String> {
        log.info("Getting tests with integrations role")
        val tests = getTestsQuery.execute()
        log.info(tests)
        return ResponseEntity.ok("¡La autenticación con integraciones ha sido exitosa!")
    }
}