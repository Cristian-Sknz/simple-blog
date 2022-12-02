package me.sknz.simpleblog.infra.security.userdetails

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import me.sknz.simpleblog.domain.model.BlogUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*

class BlogUserDetails(
    val id: UUID,
    username: String,
    val email: String,
    password: String,
    authorities: Collection<GrantedAuthority> = emptyList()
    ): User(username, password, authorities) {

    constructor(user: BlogUser): this(
        user.id,
        user.username,
        user.email,
        user.password,
        emptyList()
    )

    constructor(decodedJWT: DecodedJWT): this(
        UUID.fromString(decodedJWT.subject),
        decodedJWT.claims.getAsString("username"),
        decodedJWT.claims.getAsString("email"),
        decodedJWT.token,
        (decodedJWT.claims["authorities"]?.asList(String::class.java) ?: emptyList())
            .map(::SimpleGrantedAuthority)
    )
}
private fun Map<String, Claim>.getAsString(value: String): String {
    return this[value]!!.asString()
}