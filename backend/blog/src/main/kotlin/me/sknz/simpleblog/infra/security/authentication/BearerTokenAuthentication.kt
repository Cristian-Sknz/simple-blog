package me.sknz.simpleblog.infra.security.authentication

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class BearerTokenAuthentication(
    principal: Any,
    password: String,
    authorities: Collection<GrantedAuthority>,
    val eventId: String?
) : UsernamePasswordAuthenticationToken(principal, password, authorities)