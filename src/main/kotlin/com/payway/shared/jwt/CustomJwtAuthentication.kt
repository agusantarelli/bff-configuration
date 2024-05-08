package com.payway.shared.jwt

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class CustomJwtAuthentication(
    jwt: Jwt,
    private val siteId: String?,
    authorities: Collection<*>?
) : JwtAuthenticationToken(jwt, authorities?.map { SimpleGrantedAuthority(it.toString()) }) {

    fun getSiteId(): String? = siteId
}