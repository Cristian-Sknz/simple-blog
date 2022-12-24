package me.sknz.simpleblog.api.request

import jakarta.validation.constraints.NotEmpty

class OrganizationDTO {

    @NotEmpty
    lateinit var name: String
    var public: Boolean? = null

}