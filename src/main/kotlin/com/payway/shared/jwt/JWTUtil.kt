package com.payway.shared.jwt

import com.payway.shared.Constants
import io.jsonwebtoken.Jwts
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class JWTUtil {
    companion object {
        fun isValid(token: String, publicKey: String): Boolean {
            return try {
                val rsaPublicKey = getRSAPublicKey(publicKey)
                Jwts.parserBuilder()
                    .setSigningKey(rsaPublicKey)
                    .build()
                    .parseClaimsJws(token)
                true
            } catch (e: JwtException) {
                false
            } catch (e: Exception) {
                false
            }
        }

        private fun getRSAPublicKey(publicKey: String): java.security.PublicKey {
            val keyBytes = Base64.getDecoder().decode(publicKey)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(Constants.JWT_RSA_ALGORITHM.value)
            return keyFactory.generatePublic(keySpec)
        }

        fun decodeJwt(jwtToken: String, jwtDecoder: JwtDecoder): Jwt? {
            return try {
                jwtDecoder.decode(jwtToken)
            } catch (ex: Exception) {
                null
            }
        }
    }
}