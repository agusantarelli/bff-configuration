package com.payway.shared.config

import com.payway.shared.Constants
import com.payway.shared.jwt.CustomHeaderFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Value("\${constants.auth.publicKey}")
    private val publicKey: String = ""

    override fun configure(http: HttpSecurity) {
        http
            .addFilterAfter(CustomHeaderFilter(publicKey, jwtDecoder()), BasicAuthenticationFilter::class.java)
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/v1").authenticated()
                .anyRequest().permitAll()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val keyBytes = Base64.getDecoder().decode(publicKey)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(Constants.JWT_RSA_ALGORITHM.value)
        val rsaPublicKey = keyFactory.generatePublic(keySpec) as RSAPublicKey

        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }
}
