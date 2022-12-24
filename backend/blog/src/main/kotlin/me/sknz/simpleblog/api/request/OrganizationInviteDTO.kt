package me.sknz.simpleblog.api.request

import java.util.UUID

class OrganizationInviteDTO() {

    constructor(organization: UUID, invite: UUID): this() {
        this.organization = organization
        this.invite = invite
    }

    lateinit var organization: UUID
    lateinit var invite: UUID

    fun getInviteURL(): String {
        return "/api/invites/${invite}/join"
    }
}