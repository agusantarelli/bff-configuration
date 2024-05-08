package com.payway.adapter.utils

import com.payway.shared.jwt.CustomJwtAuthentication
import org.springframework.security.core.context.SecurityContextHolder

object Security {
    fun getSiteId(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        val customJwtAuthentication = authentication as? CustomJwtAuthentication
        return customJwtAuthentication?.getSiteId()
    }
}