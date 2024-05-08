package com.payway.shared.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomHeaderFilter(
    private val publicKey: String,
    private val jwtDecoder: JwtDecoder
) : OncePerRequestFilter() {

    private val jwtSiteIdAttributeName: String = "site_id"
    private val jwtRoleAttributeName: String = "role"

    private val AUTHORIZATION_BEARER_PREFIX = "Bearer "
    private val AUTHORIZATION_HEADER = "Authorization"
    private val ID_TOKEN_HEADER = "Id-Token"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)
        val authorizationHeaderSanitized = authorizationHeader?.removePrefix(AUTHORIZATION_BEARER_PREFIX)
        val idTokenHeader = request.getHeader(ID_TOKEN_HEADER)
        if (authorizationHeaderSanitized.isNullOrEmpty() || idTokenHeader.isNullOrEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Authorization or Id-Token header")
            return
        }

        val validAuthorizationHeader = JWTUtil.isValid(authorizationHeaderSanitized, publicKey)
        val validIdToken = JWTUtil.isValid(idTokenHeader, publicKey)
        if (!validAuthorizationHeader || !validIdToken) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            return
        }

        val idTokenDecoded = JWTUtil.decodeJwt(idTokenHeader, jwtDecoder)
        val siteId = idTokenDecoded?.claims?.get(jwtSiteIdAttributeName) as String?
        val roles = idTokenDecoded?.claims?.get(jwtRoleAttributeName)?.let { listOf(SimpleGrantedAuthority(it as String)) } ?: emptyList()

        val customJwtAuthentication = idTokenDecoded?.let { CustomJwtAuthentication(it, siteId, roles) }
        SecurityContextHolder.getContext().authentication = customJwtAuthentication

        filterChain.doFilter(request, response)
    }
}